package com.twh.ellog.process;

import com.twh.ellog.ElLogExpression;
import com.twh.ellog.ElLogExpressionParser;
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
public class ElLog {

  private static final ThreadLocal<List<String>> MDC_KEYS_THREAD_LOCAL = new ThreadLocal<>();

  private static final ElLogExpressionParser parser;

  static {
    ServiceLoader<ElLogExpressionParser> serviceLoader = ServiceLoader.load(ElLogExpressionParser.class);
    Iterator<ElLogExpressionParser> it = serviceLoader.iterator();
    if (!it.hasNext()) {
      throw new RuntimeException("implement ElLogExpressionParser interface class not found");
    }
    parser = it.next();
  }

  private ElLog() {}

  public static void entry(String elLogType, List<ElLogParam> logParams, Object... args) {
    List<String> mdcKeys = getMdcKeys();

    ElLogExpression expression = parser.parseExpression(elLogType);
    MDC.put("elLogType", expression.getValue());
    mdcKeys.add("elLogType");
    if (logParams == null || logParams.isEmpty()) {
      return;
    }
    for (ElLogParam logParam : logParams) {
      ElLogExpression keyExpr = parser.parseExpression(logParam.getKey());
      ElLogExpression valueExpr = parser.parseExpression(logParam.getValue());
      String key = keyExpr.getValue();
      if (key != null) {
        MDC.put(key, valueExpr.getValue());
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
