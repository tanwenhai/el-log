buildscript {
  repositories {
    maven {
      url = uri("https://maven.aliyun.com/repository/gradle-plugin")
    }
    mavenCentral()
    gradlePluginPortal()
  }
}

val mavenUrls = property("mavenUrls").toString()

subprojects {
  group = "com.twh"
  version = "1.0-SNAPSHOT"
  tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
    options.encoding = "UTF-8"
  }

  repositories {
    mavenUrls.split(",").forEach { repoUrl ->
      maven {
        url = uri(repoUrl.trim())
        isAllowInsecureProtocol = true
      }
    }
    mavenCentral()
  }

  apply {
    plugin("java-library")
    plugin("idea")
  }
}