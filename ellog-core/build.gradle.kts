dependencies {
  api(project(":ellog-annotation"))
  implementation("org.slf4j:slf4j-api:1.7.32")

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}