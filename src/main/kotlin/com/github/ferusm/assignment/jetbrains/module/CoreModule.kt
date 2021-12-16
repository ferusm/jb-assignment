package com.github.ferusm.assignment.jetbrains.module

import com.github.ferusm.assignment.jetbrains.util.JWTUtil.installClaimBasedRoleAuthorization
import com.github.ferusm.assignment.jetbrains.util.JWTUtil.installJwtAuthentication
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.serialization.*

fun Application.coreModule() {
    installJwtAuthentication()
    installClaimBasedRoleAuthorization()

    install(ContentNegotiation) { json() }

    install(StatusPages) {
        exception<ContentTransformationException> {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}