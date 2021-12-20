import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.Mode

plugins {
    kotlin("js")
}

group = "org.github.ferusm.assignment"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
}

val ktorVersion: String by project
val kotlinxSerializationVersion: String by project

dependencies {
    implementation(project(":packages:common"))
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-auth:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.277-kotlin-1.6.0")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:17.0.2-pre.276-kotlin-1.6.0")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.3-pre.277-kotlin-1.6.0")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:6.0.2-pre.276-kotlin-1.6.0")
}

kotlin {
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                mode = if (project.hasProperty("prod")) Mode.PRODUCTION else Mode.DEVELOPMENT
            }
        }
        useCommonJs()
    }
}