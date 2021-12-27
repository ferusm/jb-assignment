package com.github.ferusm.assignment.jetbrains.module

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.routing.*

fun Application.ui() {
    routing {
        static {
            resource("/openapi", "openapi/index.html")
            resources("/openapi/")
            resource("/", "index.html")
            resources("/")
        }
    }
}