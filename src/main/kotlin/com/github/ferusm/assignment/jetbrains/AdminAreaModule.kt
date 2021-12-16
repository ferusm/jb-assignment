package com.github.ferusm.assignment.jetbrains

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.adminAreaModule() {
    routing {
        route("/admin") {
            get("/users") {
                call.respond(HttpStatusCode.NotImplemented)
            }
        }
    }
}