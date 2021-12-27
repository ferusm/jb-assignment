package com.github.ferusm.assignment.jetbrains.module

import com.github.ferusm.assignment.jetbrains.database.UserEntity
import com.github.ferusm.assignment.jetbrains.database.UsersTable
import com.github.ferusm.assignment.jetbrains.exception.UserAlreadyExistsException
import com.github.ferusm.assignment.jetbrains.exception.WrongCredentialsException
import com.github.ferusm.assignment.jetbrains.model.Password
import com.github.ferusm.assignment.jetbrains.model.User
import com.github.ferusm.assignment.jetbrains.util.BCryptUtil
import com.github.ferusm.assignment.jetbrains.util.JWTUtil.user
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun Application.users() {
    transaction {
        SchemaUtils.create(UsersTable)
    }

    routing {
        route("/api/users") {
            authenticate {
                route("/current") {
                    get {
                        val user = call.authentication.user()
                        call.respond(HttpStatusCode.OK, user)
                    }
                    put("/password") {
                        val user = call.authentication.user()
                        val request = call.receive<Password>()
                        val encrypted = BCryptUtil.encrypt(request.password)
                        val updatedCount = transaction {
                            UsersTable.update({UsersTable.name eq user.name}) {
                                it[identifier] = encrypted
                            }
                        }
                        if (updatedCount == 0) {
                            throw WrongCredentialsException()
                        }
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }
            post {
                val request = call.receive<User>()
                val requestIdentifier = request.identifier ?: throw IllegalArgumentException("Password must be initialized")
                val encrypted = BCryptUtil.encrypt(requestIdentifier)
                val entity = transaction {
                    if (UsersTable.select { UsersTable.name eq request.name }.count() == 0L) {
                        UserEntity.new {
                            name = request.name
                            identifier = encrypted
                            role = request.role
                        }
                    } else {
                        throw UserAlreadyExistsException()
                    }
                }
                val response = User(entity.name, entity.identifier, entity.role)
                call.respond(HttpStatusCode.Created, response)
            }
        }
    }
}