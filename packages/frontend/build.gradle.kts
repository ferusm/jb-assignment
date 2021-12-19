import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.0.1-rc2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}


kotlin {
    js(IR) {
        browser {
            webpackTask {
                devServer = KotlinWebpackConfig.DevServer(proxy = mutableMapOf("/api" to "http://localhost:9090/api"))
            }
        }
        binaries.executable()
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation("app.softwork:routing-compose:0.1.4")
                implementation("io.ktor:ktor-client-core:1.6.7")
                implementation("io.ktor:ktor-client-auth:1.6.7")
                implementation("io.ktor:ktor-client-serialization:1.6.7")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
                implementation(project(":packages:common"))
                implementation(compose.web.core)
                implementation(compose.runtime)
            }
        }
    }
}