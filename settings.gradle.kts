rootProject.name = "el-log"

include(":ellog-annotation", ":ellog-core", ":ellog-spring")
include(":ellog-agent", ":ellog-processor", ":ellog-aspectj-aop")
include(":ellog-samples:post-compile-weaving")
include(":ellog-samples:spring-aop")
