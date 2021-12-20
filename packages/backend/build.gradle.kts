plugins {
    application
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.github.johnrengelman.shadow")
    id("org.openapi.generator")
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
val kotlinxSerializationVersion: String by project
val exposedVersion: String by project
val bcryptVersion: String by project
val h2Version: String by project

tasks.openApiGenerate {
    inputSpec.set("${projectDir}/openapi.yml")
    outputDir.set("${buildDir}/generated/html/openapi")
    generatorName.set("html2")
}

tasks.processResources {
    from("../react-frontend/build/distributions")
    from("${buildDir}/generated/html")
}

dependencies {
    implementation(project(":packages:common"))

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")

    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")

    implementation("at.favre.lib:bcrypt:$bcryptVersion")

    implementation("io.ktor:ktor-serialization:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("com.h2database:h2:$h2Version")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDateTimeVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}