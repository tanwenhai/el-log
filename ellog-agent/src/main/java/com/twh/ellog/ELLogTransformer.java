package com.twh.ellog;

import com.twh.ellog.annotation.ELLog;
import com.twh.ellog.annotation.ElLogParam;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.Handler;
import javassist.expr.MethodCall;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

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
        clazz.writeFile();
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
    StringBuilder srcBuilder = new StringBuilder();
    // 插入代码块，限定变量作用域
    srcBuilder.append("{");
    if (elLog.params() != null && elLog.params().length > 0) {
      srcBuilder.append("java.util.List elLogParams = new java.util.ArrayList();");
      srcBuilder.append("com.twh.ellog.process.ElLogParam elLogParam;");
      for (ElLogParam param : elLog.params()) {
        srcBuilder.append("elLogParam = new com.twh.ellog.process.ElLogParam();");
        srcBuilder.append(String.format("elLogParam.setKey(\"%s\");", param.key()));
        srcBuilder.append(String.format("elLogParam.setValue(\"%s\");", param.value()));
        srcBuilder.append("elLogParams.add(elLogParam);");
      }
    }
    srcBuilder.append(String.format("com.twh.ellog.process.ElLog.entry(\"%s\", elLogParams, $args);", elLog.logType()));
    srcBuilder.append("}");
    method.insertBefore(srcBuilder.toString());
    method.insertAfter("com.twh.ellog.process.ElLog.clean();", true);
  }
}
