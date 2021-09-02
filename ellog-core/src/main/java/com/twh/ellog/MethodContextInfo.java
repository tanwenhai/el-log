package com.twh.ellog;

import java.util.Collections;
import java.util.List;

/**
 * @author wenhai.tan
 * @date 2021/9/2
 */
public class MethodContextInfo {
  private final String name;

  private final String[] parameterVariableNames;

  private final ElLogInfo elLog;

  public MethodContextInfo(String name, String[] parameterVariableNames, ElLogInfo elLog) {
    this.name = name;
    this.parameterVariableNames = parameterVariableNames;
    this.elLog = elLog;
  }

  public String getName() {
    return name;
  }

  public String[] getParameterVariableNames() {
    return parameterVariableNames;
  }

  public ElLogInfo getElLog() {
    return elLog;
  }

  public static class ElLogInfo {
    private final String logType;

    private final List<ElLogParamInfo> params;

    public ElLogInfo(String logType, List<ElLogParamInfo> params) {
      this.logType = logType;
      this.params = Collections.unmodifiableList(params);
    }

    public String getLogType() {
      return logType;
    }

    public List<ElLogParamInfo> getParams() {
      return params;
    }
  }

  public static class ElLogParamInfo {
    private final String key;

    private final String value;

    public ElLogParamInfo(String key, String value) {
      this.key = key;
      this.value = value;
    }

    public String getKey() {
      return key;
    }

    public String getValue() {
      return value;
    }
  }

}
