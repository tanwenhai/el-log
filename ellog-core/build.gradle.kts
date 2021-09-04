dependencies {
  api(project(":ellog-annotation"))
  implementation("org.slf4j:slf4j-api:1.7.32")

  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}