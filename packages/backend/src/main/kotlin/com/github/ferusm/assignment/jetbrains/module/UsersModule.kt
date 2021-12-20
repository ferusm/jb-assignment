package com.github.ferusm.assignment.jetbrains.module

import com.github.ferusm.assignment.jetbrains.database.DatabaseProvider
import com.github.ferusm.assignment.jetbrains.database.UserEntity
import com.github.ferusm.assignment.jetbrains.database.UsersTable
import com.github.ferusm.assignment.jetbrains.exception.ConflictException
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
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.users() {
    val database = runBlocking { DatabaseProvider.get("jdbc:h2:mem:users;DB_CLOSE_DELAY=-1", "org.h2.Driver") }

    transaction(database) {
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
                        val password = call.receive<Password>().password
                        val newIdentifier = BCryptUtil.encrypt(4, password)
                        transaction {
                            UserEntity.find { UsersTable.name eq user.name }.firstOrNull()?.also {
                                it.identifier = newIdentifier
                            }
                        }
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }
            post {
                val request = call.receive<User>()
                val newIdentifier = BCryptUtil.encrypt(4, request.identifier!!)
                val entity = transaction {
                    if (UserEntity.find { UsersTable.name eq request.name }.empty()) {
                        UserEntity.new {
                            name = request.name
                            identifier = newIdentifier
                            role = request.role
                        }
                    } else {
                        throw ConflictException("User with name: ${request.name} already exists")
                    }
                }
                val response = User(entity.name, entity.identifier, entity.role)
                call.respond(HttpStatusCode.Created, response)
            }
        }
    }
}