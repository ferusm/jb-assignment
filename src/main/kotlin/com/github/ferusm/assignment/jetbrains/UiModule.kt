package com.github.ferusm.assignment.jetbrains

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.uiModule() {
    routing {
        get {
            call.respond(HttpStatusCode.NotImplemented)
        }
        get("/pa") {
            call.respond(HttpStatusCode.NotImplemented)
        }
    }
}