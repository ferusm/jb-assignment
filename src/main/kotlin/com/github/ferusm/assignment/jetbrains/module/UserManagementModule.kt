package com.github.ferusm.assignment.jetbrains.module

import com.github.ferusm.assignment.jetbrains.database.entity.UserEntity
import com.github.ferusm.assignment.jetbrains.database.table.UsersTable
import com.github.ferusm.assignment.jetbrains.exception.ConflictException
import com.github.ferusm.assignment.jetbrains.exception.NotFoundException
import com.github.ferusm.assignment.jetbrains.model.*
import com.github.ferusm.assignment.jetbrains.util.BCryptUtil
import com.github.ferusm.assignment.jetbrains.util.JWTUtil
import com.github.ferusm.assignment.jetbrains.util.JWTUtil.user
import io.ktor.application.*
import io.ktor.auth.*
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
            val encryptedPassword = BCryptUtil.encrypt(4, request.password)
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

        post("/session") {
            val credentials = call.receive<Credentials>()

            val user = transaction { UserEntity.find { UsersTable.name eq credentials.username }.firstOrNull() }
                ?: throw NotFoundException("User with name '${credentials.username}' not found")
            if (!BCryptUtil.isValid(user.password, credentials.password)) {
                throw IllegalArgumentException("Wrong password for user with name '${credentials.username}'")
            }
            val token = JWTUtil.generate(user.name, user.role, user.password, environment.config)
            val session = Session(user.name, user.role, token)
            call.respond(HttpStatusCode.Created, session)
        }

        authenticate("service") {
            put("/password") {
                val user = call.authentication.user()
                val request = call.receive<Password>()
                val encryptedPassword = BCryptUtil.encrypt(4, request.password)
                transaction {
                    val entity = UserEntity.find { UsersTable.name eq user.username }.firstOrNull()
                        ?: throw NotFoundException("User with username: ${user.username} not found")
                    entity.password = encryptedPassword
                }
                val response = user.copy(password = encryptedPassword)
                call.respond(HttpStatusCode.OK, response)
            }
        }
    }
}