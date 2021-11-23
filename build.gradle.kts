buildscript {
  repositories {
    maven {
      url = uri("https://maven.aliyun.com/repository/gradle-plugin")
    }
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins {
  java
  `java-library`
  id("nebula.dependency-recommender") version "10.0.1"
  idea
}

allprojects {
  group = "com.twh"
  version = "1.0-SNAPSHOT"

  apply(plugin = "nebula.dependency-recommender")
  apply(plugin = "java-library")

  repositories {
    MavenRepos.urls.forEach { repoUrl ->
      maven {
        url = uri(repoUrl.trim())
        isAllowInsecureProtocol = true
      }
    }
    mavenCentral()
  }

  tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
    options.encoding = "UTF-8"
  }

  dependencyRecommendations {
    map(
      mapOf(
        "recommendations" to mapOf(
          "org.springframework:spring-expression" to Versions.SPRING_VERSION,
          "org.junit.jupiter:junit-jupiter-api" to Versions.JUNIT_JUPITER,
          "org.junit.jupiter:junit-jupiter-engine" to Versions.JUNIT_JUPITER
        )
      )
    )
  }
}