package com.github.ferusm.assignment.jetbrains

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.reviewerAreaModule() {
    routing {
        route("/reviewer") {
            get("/metrics") {
                call.respond(HttpStatusCode.NotImplemented)
            }
        }
    }
}