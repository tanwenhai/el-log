package com.twh.ellog.spring;

import com.twh.ellog.ElLogExpressionParser;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

/**
 * @author wenhai.tan
 * @date 2021/9/2
 */
public class SpringElLogExpression implements ElLogExpressionParser {

  private final ExpressionParser parser;

  private final EvaluationContext context;

  public SpringElLogExpression(ExpressionParser parser, EvaluationContext context) {
    this.parser = parser;
    this.context = context;
  }

  @Override
  public String getValue(String expressionString) {
    return parser.parseExpression(expressionString).getValue(context, String.class);
  }
}
