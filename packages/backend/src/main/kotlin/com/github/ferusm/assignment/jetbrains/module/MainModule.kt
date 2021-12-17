package com.github.ferusm.assignment.jetbrains.module

import com.github.ferusm.assignment.jetbrains.exception.ConflictException
import com.github.ferusm.assignment.jetbrains.exception.NotFoundException
import com.github.ferusm.assignment.jetbrains.util.JWTUtil.installClaimBasedRoleAuthorization
import com.github.ferusm.assignment.jetbrains.util.JWTUtil.installJwtAuthentication
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.serialization.*

fun Application.main() {
    installJwtAuthentication()
    installClaimBasedRoleAuthorization()

    install(ContentNegotiation) { json() }

    install(StatusPages) {
        exception<ContentTransformationException> {
            call.respond(HttpStatusCode.BadRequest, it.message ?: it.localizedMessage)
        }
        exception<ConflictException> {
            call.respond(HttpStatusCode.Conflict, it.message ?: it.localizedMessage)
        }
        exception<NotFoundException> {
            call.respond(HttpStatusCode.NotFound, it.message ?: it.localizedMessage)
        }
    }
}