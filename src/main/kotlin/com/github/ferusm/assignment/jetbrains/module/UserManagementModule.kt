package com.github.ferusm.assignment.jetbrains.module


import com.github.ferusm.assignment.jetbrains.model.Credentials
import com.github.ferusm.assignment.jetbrains.model.Role
import com.github.ferusm.assignment.jetbrains.model.Session
import com.github.ferusm.assignment.jetbrains.model.User
import com.github.ferusm.assignment.jetbrains.util.JWTUtil
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.userManagementModule() {
    val users = setOf(
        User("admin", "admin", Role.ADMIN),
        User("reviewer", "reviewer", Role.REVIEWER),
        User("user", "user", Role.USER)
    )

    routing {
        post("/user") {
            call.respond(HttpStatusCode.NotImplemented)
        }
        post("/session") {
            val credentials = call.receive<Credentials>()

            val user = users.find { it.username == credentials.username }
            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, "Wrong username")
                return@post
            }

            if (user.password != credentials.password) {
                call.respond(HttpStatusCode.BadRequest, "Wrong password")
            }

            val token = JWTUtil.generate(user.username, user.role, environment.config)
            val session = Session(user.username, user.role, token)
            call.respond(HttpStatusCode.OK, session)
        }
        authenticate("service") {
            put("/password") {
                call.respond(HttpStatusCode.NotImplemented)
            }
        }
    }
}