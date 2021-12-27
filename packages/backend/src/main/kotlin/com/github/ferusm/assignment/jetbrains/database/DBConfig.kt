package com.github.ferusm.assignment.jetbrains.database

import io.ktor.config.*

class DBConfig(config: ApplicationConfig) {
    val url: String = config.property("ktor.database.url").getString()
    val driver: String = config.property("ktor.database.driver").getString()
}