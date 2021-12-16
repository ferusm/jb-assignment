package com.github.ferusm.assignment.jetbrains.module

import com.github.ferusm.assignment.jetbrains.feature.withRole
import com.github.ferusm.assignment.jetbrains.model.Role
import com.github.ferusm.assignment.jetbrains.util.JWTUtil.installClaimBasedRoleAuthorization
import com.github.ferusm.assignment.jetbrains.util.JWTUtil.installJwtAuthentication
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.adminAreaModule() {
    routing {
        authenticate("service") {
            withRole(Role.ADMIN) {
                route("/admin") {
                    get("/users") {
                        call.respond(HttpStatusCode.NotImplemented)
                    }
                }
            }
        }
    }
}