package com.github.ferusm.assignment.jetbrains

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.userAreaModule() {
    routing {
        route("/user") {
            get("/session") {
                call.respond(HttpStatusCode.NotImplemented)
            }
        }
    }
}