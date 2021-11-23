package com.twh.ellog.samples.apt;

import com.twh.ellog.annotation.ELLog;

/**
 * @author wenhai.tan
 * @date 2021/11/23
 */
public class Account {
  @ELLog(logType = "test")
  public void logTest() {
    System.out.println(1);
  }

  public static void main(String[] args) {
    new Account().logTest();
  }
}
