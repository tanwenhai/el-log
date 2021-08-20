package com.twh.ellog.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wenhai.tan
 * @date 2021/8/16
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface ElLogParam {

  /**
   * spring el
   * @return el string
   */
  String key();

  /**
   * spring el
   * @return el string
   */
  String value();
}
