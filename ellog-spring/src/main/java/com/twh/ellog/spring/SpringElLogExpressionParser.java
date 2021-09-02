package com.twh.ellog.spring;

import com.twh.ellog.ElLogExpressionParser;
import com.twh.ellog.ElLogExpressionEngine;
import com.twh.ellog.MethodContextInfo;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author wenhai.tan
 * @date 2021/9/2
 */
public class SpringElLogExpressionParser implements ElLogExpressionEngine {

  @Override
  public ElLogExpressionParser buildParser(MethodContextInfo methodContextInfo, Object... args) {
    EvaluationContext context = constructContext(methodContextInfo, args);
    ExpressionParser spelParser = new SpelExpressionParser();

    return new SpringElLogExpression(spelParser, context);
  }

  private EvaluationContext constructContext(MethodContextInfo methodContextInfo, Object[] args) {
    EvaluationContext context = new StandardEvaluationContext();
    String[] parameterNames = methodContextInfo.getParameterVariableNames();

    context.setVariable("methodName", methodContextInfo.getName());
    if (args == null || args.length == 0) {
      return context;
    }

    for (int i = 0; i < args.length; i++) {
      context.setVariable(parameterNames[i], args[i]);
    }
    return context;
  }
}
