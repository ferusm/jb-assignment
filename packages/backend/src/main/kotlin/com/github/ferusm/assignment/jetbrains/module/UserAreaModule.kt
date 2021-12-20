package com.github.ferusm.assignment.jetbrains.module

import com.github.ferusm.assignment.jetbrains.feature.withRole
import com.github.ferusm.assignment.jetbrains.model.Role
import com.github.ferusm.assignment.jetbrains.util.JWTUtil.user
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.userArea() {
    routing {
        authenticate {
            withRole(Role.USER) {
                get("/api/user/hello") {
                    val user = call.authentication.user()
                    call.respond(HttpStatusCode.OK, "You’re a ${Role.USER}, ${user.name}")
                }
            }
        }
    }
}