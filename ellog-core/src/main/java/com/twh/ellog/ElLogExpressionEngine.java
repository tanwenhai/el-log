package com.twh.ellog;

/**
 * @author wenhai.tan
 * @date 2021/8/20
 */
public interface ElLogExpressionEngine {
  ElLogExpressionParser buildParser(MethodContextInfo methodContextInfo, Object... args);
}
