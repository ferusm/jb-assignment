package com.github.ferusm.assignment.jetbrains.module

import com.github.ferusm.assignment.jetbrains.database.DBConfig
import com.github.ferusm.assignment.jetbrains.exception.UserAlreadyExistsException
import com.github.ferusm.assignment.jetbrains.exception.InvalidTokenException
import com.github.ferusm.assignment.jetbrains.exception.WrongCredentialsException
import com.github.ferusm.assignment.jetbrains.util.JWTConfig
import com.github.ferusm.assignment.jetbrains.util.JWTUtil.installClaimBasedRoleAuthorization
import com.github.ferusm.assignment.jetbrains.util.JWTUtil.installJwtAuthentication
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.serialization.*
import org.jetbrains.exposed.sql.Database

fun Application.main() {
    val dBConfig = DBConfig(environment.config)
    Database.connect(dBConfig.url, dBConfig.driver)

    val jwtConfig = JWTConfig(environment.config)
    installJwtAuthentication(jwtConfig)

    installClaimBasedRoleAuthorization()

    install(ContentNegotiation) { json() }

    install(StatusPages) {
        exception<UserAlreadyExistsException> {
            call.respond(HttpStatusCode.Conflict, it.message ?: it.localizedMessage)
        }
        exception<WrongCredentialsException> {
            call.respond(HttpStatusCode.Unauthorized, it.message ?: it.localizedMessage)
        }
        exception<InvalidTokenException> {
            call.respond(HttpStatusCode.Unauthorized, it.message ?: it.localizedMessage)
        }
    }
}