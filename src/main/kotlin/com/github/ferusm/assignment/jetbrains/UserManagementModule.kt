package com.github.ferusm.assignment.jetbrains

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.userManagementModule() {
    routing {
        post("/user") {
            call.respond(HttpStatusCode.NotImplemented)
        }
        post("/session") {
            call.respond(HttpStatusCode.NotImplemented)
        }
        put("/password") {
            call.respond(HttpStatusCode.NotImplemented)
        }
    }
}