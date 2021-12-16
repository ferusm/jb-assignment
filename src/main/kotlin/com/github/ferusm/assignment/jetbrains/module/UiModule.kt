package com.github.ferusm.assignment.jetbrains.module

import com.github.ferusm.assignment.jetbrains.util.JWTUtil.installJwtAuthentication
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.uiModule() {
    routing {
        get {
            call.respond(HttpStatusCode.NotImplemented)
        }
        authenticate("service") {
            get("/pa") {
                call.respond(HttpStatusCode.NotImplemented)
            }
        }
    }
}