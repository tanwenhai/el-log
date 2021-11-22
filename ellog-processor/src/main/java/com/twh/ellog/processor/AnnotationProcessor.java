package com.twh.ellog.processor;

import com.google.auto.service.AutoService;
import com.twh.ellog.MethodContextInfo;
import com.twh.ellog.annotation.ELLog;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author wenhai.tan
 * @date 2021/11/19
 */
@AutoService(AnnotationProcessor.class)
public class AnnotationProcessor extends AbstractProcessor {

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    List<ExecutableElement> methodElements = roundEnv.getRootElements().stream()
        .filter(element -> element.getKind() == ElementKind.METHOD)
        .map(ExecutableElement.class::cast)
        .collect(Collectors.toList());
    for (ExecutableElement methodElement : methodElements) {
      MethodContextInfo methodContextInfo = parseMethod(methodElement);
      if (methodContextInfo == null) {
        continue;
      }
      // 修改字节码
      System.out.println(1);
    }

    return false;
  }

  private MethodContextInfo parseMethod(ExecutableElement methodElement) {
    ELLog logAnnotation = methodElement.getAnnotation(ELLog.class);
    if (logAnnotation == null) {
      return null;
    }

    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "modify " + methodElement.getSimpleName() + " code");
    String[] parameterVariableNames = new String[methodElement.getParameters().size()];
    for (int i = 0; i < parameterVariableNames.length; i++) {
      parameterVariableNames[i] = methodElement.getParameters().get(i).getSimpleName().toString();
    }

    return new MethodContextInfo(methodElement.getSimpleName().toString(), parameterVariableNames, MethodContextInfo.ElLogInfo.valueOf(logAnnotation));
  }
}
