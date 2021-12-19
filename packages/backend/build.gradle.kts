plugins {
    application
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.1"
}

group = "com.github.ferusm.assignment.jetbrains"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

val kotlinVersion: String by project
val ktorVersion: String by project
val kotlinxDateTimeVersion: String by project
val logbackVersion: String by project


tasks.processResources {
    from("../frontend/build/distributions")
}

dependencies {
    implementation(project(":packages:common"))

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")

    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")

    implementation("at.favre.lib:bcrypt:0.9.0")

    implementation("io.ktor:ktor-serialization:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")

    implementation("org.jetbrains.exposed", "exposed-core", "0.34.1")
    implementation("org.jetbrains.exposed", "exposed-dao", "0.34.1")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.34.1")
    implementation("com.h2database:h2:1.4.199")

    implementation("io.insert-koin:koin-core:3.1.4")
    implementation("io.insert-koin:koin-ktor:3.1.4")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDateTimeVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}