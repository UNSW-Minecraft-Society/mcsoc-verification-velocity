plugins {
    kotlin("jvm") version "1.9.22"
}

group = "com.mcsoc.verificationvelocity"

version = "0.0.1"
val velocity_api_version="3.5.0-SNAPSHOT"


repositories {
  maven {
    name = "papermc"
    url = uri("https://repo.papermc.io/repository/maven-public/")
  }
}

dependencies {
  compileOnly("com.velocitypowered:velocity-api:${velocity_api_version}")
  annotationProcessor("com.velocitypowered:velocity-api:${velocity_api_version}")
}