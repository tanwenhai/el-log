dependencies {
  implementation(project(":ellog-core"))
  implementation("org.javassist:javassist:3.28.0-GA")

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
  testImplementation(project(":ellog-spring"))
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
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