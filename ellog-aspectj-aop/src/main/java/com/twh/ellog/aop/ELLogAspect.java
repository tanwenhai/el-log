package com.twh.ellog.aop;

import com.twh.ellog.Metadata;
import com.twh.ellog.MethodContextInfo;
import com.twh.ellog.annotation.ELLog;
import com.twh.ellog.process.MethodMonitor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wenhai.tan
 * @date 2021/11/23
 */
@Aspect
public class ELLogAspect {

  @SuppressWarnings("squid:S1186")
  @Pointcut("@annotation(logAnnotation)")
  public void callAt(ELLog logAnnotation) {
  }

  @Around(value = "callAt(logAnnotation)", argNames = "pjp,logAnnotation")
  public Object around(ProceedingJoinPoint pjp, ELLog logAnnotation) throws Throwable {
    try {
      Map<String, Object> nameAndValue = getNameAndValue(pjp);
      String methodName = getMethodName(pjp);
      MethodContextInfo methodContextInfo = new MethodContextInfo(methodName, nameAndValue.keySet().toArray(new String[]{}), MethodContextInfo.ElLogInfo.valueOf(logAnnotation));
      String id = Metadata.newMetadataGetKey(methodContextInfo);
      MethodMonitor.entry(id, pjp.getArgs());
      return pjp.proceed();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      MethodMonitor.clean();
    }
  }

  private Map<String, Object> getNameAndValue(ProceedingJoinPoint pjp) {
    if (pjp.getArgs() == null || pjp.getArgs().length == 0) {
      return Collections.emptyMap();
    }
    Object[] args = pjp.getArgs();
    String[] argNames = ((CodeSignature)pjp.getSignature()).getParameterNames();

    Map<String, Object> maps = new HashMap<>(args.length, 1.0F);
    for (int i = 0; i < args.length; i++) {
      maps.put(argNames[i], args[i]);
    }

    return maps;
  }

  private String getMethodName(ProceedingJoinPoint pjp) {
    try {
      return ((MethodSignature)pjp.getSignature()).getMethod().getName();
    } catch (Exception e) {
      e.printStackTrace();
      return "unknow";
    }
  }
}
