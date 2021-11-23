plugins {
  id("io.freefair.aspectj.post-compile-weaving") version("6.3.0")
}
dependencies {
  implementation("org.aspectj","aspectjrt", "1.9.7")
  implementation(project(":ellog-core"))
  aspect(project(":ellog-aspectj-aop"))
}
