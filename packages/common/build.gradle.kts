plugins {
    kotlin("multiplatform")
    `maven-publish`
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
}

group = "com.github.ferusm.assignment.jetbrains"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    jvm()
    js(IR) { browser() }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.1")
            }
        }
    }
}
