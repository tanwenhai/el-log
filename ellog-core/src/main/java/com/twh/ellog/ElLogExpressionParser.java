package com.twh.ellog;

/**
 * @author wenhai.tan
 * @date 2021/8/20
 */
public interface ElLogExpressionParser {
  ElLogExpression parseExpression(Object... args);
}
