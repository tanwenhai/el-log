package com.twh.ellog.process;

import com.twh.ellog.ElLogExpressionEngine;
import com.twh.ellog.ElLogExpressionParser;
import com.twh.ellog.Metadata;
import com.twh.ellog.MethodContextInfo;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author wenhai.tan
 * @date 2021/8/17
 */
@SuppressWarnings({"squid:S112", "squid:S5164"})
public class MethodMonitor {

  private static final ThreadLocal<List<String>> MDC_KEYS_THREAD_LOCAL = new ThreadLocal<>();

  private static final ElLogExpressionEngine expressionEngine;

  static {
    ServiceLoader<ElLogExpressionEngine> serviceLoader = ServiceLoader.load(ElLogExpressionEngine.class);
    Iterator<ElLogExpressionEngine> it = serviceLoader.iterator();
    if (!it.hasNext()) {
      throw new RuntimeException("implement ElLogExpressionParser interface class not found");
    }
    expressionEngine = it.next();
  }

  private MethodMonitor() {
  }

  public static void entry(String metadataId, Object... args) {
    Metadata<MethodContextInfo> metadata = Metadata.getById(metadataId);
    if (metadata == null || metadata.getData() == null) {
      return;
    }
    MethodContextInfo methodContextInfo = metadata.getData();
    MethodContextInfo.ElLogInfo elLogInfo = methodContextInfo.getElLog();
    List<MethodContextInfo.ElLogParamInfo> logParams = elLogInfo.getParams();
    List<String> mdcKeys = getMdcKeys();

    ElLogExpressionParser parser = expressionEngine.buildParser(methodContextInfo, args);
    MDC.put("elLogType", parser.getValue(elLogInfo.getLogType()));
    mdcKeys.add("elLogType");
    if (logParams == null || logParams.isEmpty()) {
      return;
    }
    for (MethodContextInfo.ElLogParamInfo logParam : logParams) {
      String key = parser.getValue(logParam.getKey());
      if (key != null) {
        MDC.put(key, parser.getValue(logParam.getValue()));
        mdcKeys.add(key);
      }
    }
  }

  private static List<String> getMdcKeys() {
    List<String> mdcKeys = MDC_KEYS_THREAD_LOCAL.get();
    if (mdcKeys == null) {
      mdcKeys = new ArrayList<>();
      MDC_KEYS_THREAD_LOCAL.set(mdcKeys);
    }

    return mdcKeys;
  }

  public static void clean() {
    for (String mdcKey : getMdcKeys()) {
      MDC.remove(mdcKey);
    }
  }
}
