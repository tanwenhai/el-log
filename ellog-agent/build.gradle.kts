dependencies {
  implementation(project(":ellog-core"))
  implementation("org.javassist:javassist:3.28.0-GA")

  testImplementation(project(":ellog-spring"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.jar {
  manifest {
    attributes(
      "Can-Redefine-Classes" to true,
      "Can-Retransform-Classes" to true,
      "Premain-Class" to "com.twh.ellog.ELLogAgent"
    )
  }
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}