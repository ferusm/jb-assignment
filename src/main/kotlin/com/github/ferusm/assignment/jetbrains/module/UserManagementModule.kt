package com.github.ferusm.assignment.jetbrains.module


import at.favre.lib.crypto.bcrypt.BCrypt
import com.github.ferusm.assignment.jetbrains.database.entity.UserEntity
import com.github.ferusm.assignment.jetbrains.database.table.UsersTable
import com.github.ferusm.assignment.jetbrains.exception.ConflictException
import com.github.ferusm.assignment.jetbrains.exception.NotFoundException
import com.github.ferusm.assignment.jetbrains.model.Credentials
import com.github.ferusm.assignment.jetbrains.model.Role
import com.github.ferusm.assignment.jetbrains.model.Session
import com.github.ferusm.assignment.jetbrains.model.User
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

    transaction(database) {
        SchemaUtils.create(UsersTable)
    }

    routing {
        post("/user") {
            val request = call.receive<User>()
            val encryptedPassword = BCrypt.withDefaults().hashToString(4, request.password.toCharArray())
            val entity = transaction {
                if (UserEntity.find { UsersTable.name eq request.username }.empty()) {
                    UserEntity.new {
                        name = request.username
                        password = encryptedPassword
                        role = request.role
                    }
                } else {
                    throw ConflictException("User with name '${request.username}' already exists")
                }
            }
            val response = User(entity.name, entity.password, entity.role)
            call.respond(HttpStatusCode.Created, response)
        }
        put("/password") {
            call.respond(HttpStatusCode.NotImplemented)
        }
        post("/session") {
            val credentials = call.receive<Credentials>()

            val user = transaction { UserEntity.find { UsersTable.name eq credentials.username }.firstOrNull() }
                ?: throw NotFoundException("User with name '${credentials.username}' not found")
            val isPasswordValid = BCrypt.verifyer().verify(credentials.password.toCharArray(), user.password).verified
            if (!isPasswordValid) {
                throw IllegalArgumentException("Wrong password")
            }
            val token = JWTUtil.generate(user.name, user.role, environment.config)
            val session = Session(user.name, user.role, token)
            call.respond(HttpStatusCode.Created, session)
        }
    }
}