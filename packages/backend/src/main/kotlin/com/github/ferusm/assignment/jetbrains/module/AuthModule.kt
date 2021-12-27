package com.github.ferusm.assignment.jetbrains.module


import com.github.ferusm.assignment.jetbrains.database.UserEntity
import com.github.ferusm.assignment.jetbrains.database.UsersTable
import com.github.ferusm.assignment.jetbrains.exception.InvalidTokenException
import com.github.ferusm.assignment.jetbrains.exception.WrongCredentialsException
import com.github.ferusm.assignment.jetbrains.model.Credentials
import com.github.ferusm.assignment.jetbrains.model.RefreshToken
import com.github.ferusm.assignment.jetbrains.model.TokenPair
import com.github.ferusm.assignment.jetbrains.util.BCryptUtil
import com.github.ferusm.assignment.jetbrains.util.JWTConfig
import com.github.ferusm.assignment.jetbrains.util.JWTUtil
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun Application.auth() {
    transaction {
        SchemaUtils.create(UsersTable)
    }

    val jwtConfig = JWTConfig(environment.config)

    routing {
        post("/api/tokens") {
            val request = call.receive<Credentials>()

            val refresh = JWTUtil.generateRefreshToken(jwtConfig)

            val access = transaction {
                val user = UserEntity.find { UsersTable.name eq request.name }.firstOrNull()
                    ?: throw WrongCredentialsException()

                val isPasswordValid = BCryptUtil.isValid(user.identifier, request.identifier)
                if (!isPasswordValid) {
                    throw WrongCredentialsException()
                }

                user.refreshToken = refresh

                JWTUtil.generateAccessToken(user.name, user.role.name, jwtConfig)
            }
            val tokenPair = TokenPair(access, refresh)
            call.respond(HttpStatusCode.Created, tokenPair)
        }

        post("/api/refresh") {
            val request = call.receive<RefreshToken>()
            if (!JWTUtil.isValidToken(request.refresh, jwtConfig)) {
                throw InvalidTokenException()
            }
            val refresh = JWTUtil.generateRefreshToken(jwtConfig)
            val access = transaction {
                val user = UserEntity.find { UsersTable.refreshToken eq  request.refresh}.firstOrNull() ?: throw InvalidTokenException()

                user.refreshToken = refresh

                JWTUtil.generateAccessToken(user.name, user.role.name, jwtConfig)
            }
            val tokenPair = TokenPair(access, refresh)
            call.respond(HttpStatusCode.OK, tokenPair)
        }

        post("/api/logout") {
            val token = call.receive<RefreshToken>().refresh
            val updatedCount = transaction {
                UsersTable.update({ UsersTable.refreshToken eq token }) {
                    it[refreshToken] = ""
                }
            }
            if (updatedCount <= 0) {
                throw InvalidTokenException()
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}