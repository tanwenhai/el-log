package com.twh.ellog;

import com.twh.ellog.annotation.ELLog;
import com.twh.ellog.annotation.ElLogParam;

/**
 * @author wenhai.tan
 * @date 2021/8/16
 */
public class AgentTest {
  public static void main(String[] args) {
    new AgentTest().test(1, 3);
  }

  @ELLog(logType = "'test'", params = {
      @ElLogParam(key = "'aa'", value = "'Hello World'.concat('!')"),
      @ElLogParam(key = "'cc'", value = "#{ T(java.lang.Math).random() * 100.0 }"),
  })
  public void test(int a, int b) {
    System.out.println("aa");
    System.out.println("bb");
    System.out.println("cc");
    System.out.println("dd");
    int c = a / b;
  }
}
