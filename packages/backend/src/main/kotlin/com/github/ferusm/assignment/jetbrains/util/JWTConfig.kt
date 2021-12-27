package com.github.ferusm.assignment.jetbrains.util


import io.ktor.config.*
import kotlin.time.Duration

class JWTConfig(config: ApplicationConfig) {
    val secret: String = config.property("ktor.jwt.secret").getString()
    val issuer: String = config.property("ktor.jwt.issuer").getString()
    val audience: String = config.property("ktor.jwt.audience").getString()
    val realm: String = config.property("ktor.jwt.realm").getString()
    val accessTokenTtl: Duration = Duration.parse(config.property("ktor.jwt.accessTtl").getString())
    val refreshTokenTtl: Duration = Duration.parse(config.property("ktor.jwt.refreshTtl").getString())
}