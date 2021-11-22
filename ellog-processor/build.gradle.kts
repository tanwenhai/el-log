dependencies {
  implementation(project(":ellog-core"))
  implementation("com.google.auto.service:auto-service-annotations:1.0.1")
  implementation(files(System.getProperty("java.home") + "/lib/tools.jar"))

  annotationProcessor("com.google.auto.service:auto-service:1.0.1")
}