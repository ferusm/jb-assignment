package com.github.ferusm.assignment.jetbrains.module


import at.favre.lib.crypto.bcrypt.BCrypt
import com.github.ferusm.assignment.jetbrains.database.entity.UserEntity
import com.github.ferusm.assignment.jetbrains.database.table.UsersTable
import com.github.ferusm.assignment.jetbrains.model.Credentials
import com.github.ferusm.assignment.jetbrains.model.Role
import com.github.ferusm.assignment.jetbrains.model.Session
import com.github.ferusm.assignment.jetbrains.util.JWTUtil
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.userManagementModule() {
    val database = Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

    val superuserName = environment.config.property("ktor.superuser.name").getString()
    val superuserPassword = environment.config.property("ktor.superuser.password").getString()

    transaction(database) {
        SchemaUtils.create(UsersTable)
        val isSuperuserExists = !UserEntity.find { UsersTable.name eq superuserName }.empty()
        if (!isSuperuserExists) {
            UserEntity.new {
                name = superuserName
                password = superuserPassword
                role = Role.ADMIN
            }
        }
    }

    routing {
        post("/user") {
            call.respond(HttpStatusCode.NotImplemented)
        }
        put("/password") {
            call.respond(HttpStatusCode.NotImplemented)
        }
        post("/session") {
            val credentials = call.receive<Credentials>()

            val user = transaction { UserEntity.find { UsersTable.name eq credentials.username }.firstOrNull() }
            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, "Wrong username")
                return@post
            }
            val isPasswordValid = BCrypt.verifyer().verify(credentials.password.toCharArray(), user.password).verified
            if (!isPasswordValid) {
                call.respond(HttpStatusCode.BadRequest, "Wrong password")
                return@post
            }

            val token = JWTUtil.generate(user.name, user.role, environment.config)

            val session = Session(user.name, user.role, token)
            call.respond(HttpStatusCode.OK, session)
        }
    }
}