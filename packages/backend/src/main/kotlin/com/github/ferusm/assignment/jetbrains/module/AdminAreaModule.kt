package com.github.ferusm.assignment.jetbrains.module

import com.github.ferusm.assignment.jetbrains.feature.withRole
import com.github.ferusm.assignment.jetbrains.role.AdminRole
import com.github.ferusm.assignment.jetbrains.util.JWTUtil.user
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.adminArea() {
    routing {
        authenticate {
            withRole(AdminRole) {
                get("/api/admin/hello") {
                    val user = call.authentication.user()
                    call.respond(HttpStatusCode.OK, "You’re a ${AdminRole.name}, ${user.name}")
                }
            }
        }
    }
}