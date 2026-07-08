plugins {
    kotlin("jvm") version "2.4.0"
    id("com.gradleup.shadow") version "9.5.1"
    id("org.jetbrains.kotlin.kapt") version "2.4.20-Beta1"
}

group = "com.mcsoc.verificationvelocity"

version = "0.1.2"
val velocity_api_version="3.5.0-SNAPSHOT"


repositories {
  mavenCentral()

  maven {
    name = "papermc"
    url = uri("https://repo.papermc.io/repository/maven-public/")
  }
}

dependencies {
  compileOnly("com.velocitypowered:velocity-api:${velocity_api_version}")
  annotationProcessor("com.velocitypowered:velocity-api:${velocity_api_version}")
  kapt("com.velocitypowered:velocity-api:$velocity_api_version")

  implementation("org.jetbrains.exposed:exposed-core:1.3.1")
  implementation("org.jetbrains.exposed:exposed-jdbc:1.3.1")
  implementation("org.jetbrains.exposed:exposed-dao:1.3.1") // Optional
  
  implementation("com.h2database:h2:2.4.240")
}

tasks {
  shadowJar {
    archiveClassifier.set("")         
    duplicatesStrategy = DuplicatesStrategy.WARN 
    mergeServiceFiles()
  }
  build {
    dependsOn(shadowJar)
  }
}