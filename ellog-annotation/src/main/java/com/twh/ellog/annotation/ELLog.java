package com.twh.ellog.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * <pre>
 *   @ELLog(logType = "test", params = {
 *       @ElLogParam(key = "", value = "")
 *   })
 * </pre>
 * @author xixi
 * @date 2021/8/16
 */
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ELLog {

  String logType();

  ElLogParam[] params() default {};
}
