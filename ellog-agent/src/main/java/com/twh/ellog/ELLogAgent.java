package com.twh.ellog;

import java.lang.instrument.Instrumentation;

/**
 * @author xixi
 * @date 2021/8/16
 */
@SuppressWarnings({"squid:S1118", "squid:S1172"})
public class ELLogAgent {
  public static void premain(String agentArgs, Instrumentation inst) {
    inst.addTransformer(new ELLogTransformer(agentArgs), true);
  }
}
