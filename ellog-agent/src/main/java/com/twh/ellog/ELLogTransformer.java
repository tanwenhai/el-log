package com.twh.ellog;

import com.twh.ellog.annotation.ELLog;
import com.twh.ellog.annotation.ElLogParam;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xixi
 * @date 2021/8/16
 */
public class ELLogTransformer implements ClassFileTransformer {

  private final String packagePrefix;

  private final ClassPool classPool;

  public ELLogTransformer(String packagePrefix) {
    this.packagePrefix = packagePrefix;
    classPool = ClassPool.getDefault();
  }

  @SuppressWarnings("squid:S1168")
  @Override
  public byte[] transform(ClassLoader loader,
                          String className,
                          Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          byte[] classfileBuffer) throws IllegalClassFormatException {
    if (!className.startsWith(packagePrefix)) {
      return null;
    }

    try {
      final CtClass clazz = classPool.get(className.replace("/", "."));
      CtMethod[] methodList = clazz.getDeclaredMethods();
      boolean modify = false;
      for (CtMethod ctMethod : methodList) {
        ELLog elLog = (ELLog) ctMethod.getAnnotation(ELLog.class);
        if (elLog != null) {
          decorate(ctMethod, elLog);
          modify = true;
        }
      }

      if (modify) {
        byte[] byteCode = clazz.toBytecode();
        //detach的意思是将内存中曾经被javassist加载过的Date对象移除，如果下次有需要在内存中找不到会重新走javassist加载
        clazz.detach();
        return byteCode;
      }
      clazz.detach();
      // 如果返回null则字节码不会被修改
      return null;
    } catch (Exception e) {
      throw new IllegalClassFormatException(e.getClass() + ":" + e.getMessage());
    }
  }

  private void decorate(CtMethod method, ELLog elLog) throws CannotCompileException {
    String[] parameterVariableNames = getMethodParamNames(method);
    StringBuilder srcBuilder = new StringBuilder();
    List<MethodContextInfo.ElLogParamInfo> paramInfoList = new ArrayList<>();
    // 插入代码块，限定变量作用域
    srcBuilder.append("{");
    if (elLog.params() != null && elLog.params().length > 0) {
      for (ElLogParam param : elLog.params()) {
        MethodContextInfo.ElLogParamInfo paramInfo = new MethodContextInfo.ElLogParamInfo(param.key(), param.value());
        paramInfoList.add(paramInfo);
      }
    }
    MethodContextInfo.ElLogInfo elLogInfo = new MethodContextInfo.ElLogInfo(elLog.logType(), paramInfoList);
    MethodContextInfo methodContextInfo = new MethodContextInfo(method.getName(), parameterVariableNames, elLogInfo);
    String id = Metadata.newMetadataGetKey(methodContextInfo);
    srcBuilder.append(String.format("com.twh.ellog.process.MethodMonitor.entry(\"%s\", $args);", id));
    srcBuilder.append("}");
    method.insertBefore(srcBuilder.toString());
    method.insertAfter("com.twh.ellog.process.MethodMonitor.clean();", true);
  }

  @SuppressWarnings("squid:S112")
  private String[] getMethodParamNames(CtMethod ctMethod) {
    CtClass cc = ctMethod.getDeclaringClass();
    MethodInfo methodInfo = ctMethod.getMethodInfo();
    CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
    LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
    if (attr == null) {
      throw new RuntimeException(cc.getName());
    }

    String[] paramNames;
    try {
      paramNames = new String[ctMethod.getParameterTypes().length];
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    }
    int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
    for (int i = 0; i < paramNames.length; i++) {
      paramNames[i] = attr.variableName(i + pos);
    }
    return paramNames;
  }
}
