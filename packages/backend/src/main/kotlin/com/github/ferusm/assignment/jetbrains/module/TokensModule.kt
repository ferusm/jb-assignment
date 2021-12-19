package com.github.ferusm.assignment.jetbrains.module

import com.github.ferusm.assignment.jetbrains.database.DatabaseProvider
import com.github.ferusm.assignment.jetbrains.database.UserEntity
import com.github.ferusm.assignment.jetbrains.database.UsersTable
import com.github.ferusm.assignment.jetbrains.model.*
import com.github.ferusm.assignment.jetbrains.model.TokenPair
import com.github.ferusm.assignment.jetbrains.util.BCryptUtil
import com.github.ferusm.assignment.jetbrains.util.JWTUtil
import io.ktor.application.*
import io.ktor.application.Application
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.tokens() {
    val database = runBlocking { DatabaseProvider.get("jdbc:h2:mem:users;DB_CLOSE_DELAY=-1", "org.h2.Driver") }

    transaction(database) {
        SchemaUtils.create(UsersTable)
    }

    routing {
        post("/api/tokens") {
            val credentials = call.receive<Credentials>()
            val user = transaction(database) {
                UserEntity.find { UsersTable.name eq credentials.name }.firstOrNull()
            } ?: throw NotFoundException("User not found")
            val isPasswordValid = BCryptUtil.isValid(user.identifier, credentials.identifier)
            if (!isPasswordValid) {
                throw BadRequestException("Wrong password")
            }
            val access = JWTUtil.generateAccessToken(user.name, user.role, user.identifier, environment.config)
            val refresh = JWTUtil.generateRefreshToken(environment.config)
            transaction(database) {
                UserEntity.findById(user.id)?.apply { refreshToken = refresh }
            } ?: throw NotFoundException("User not found")
            val tokenPair = TokenPair(access, refresh)
            call.respond(HttpStatusCode.Created, tokenPair)
        }
        post("/tokens/refresh") {
            val token = call.receive<RefreshToken>().refresh
            if (!JWTUtil.isValidRefreshToken(token, environment.config)) {
                throw BadRequestException("Invalid token")
            }
            val user = transaction(database) {
                UserEntity.find { UsersTable.refreshToken eq token }.firstOrNull()
            } ?: throw BadRequestException("Invalid token")
            val access = JWTUtil.generateAccessToken(user.name, user.role, user.identifier, environment.config)
            val refresh = JWTUtil.generateRefreshToken(environment.config)
            transaction {
                UserEntity.find { UsersTable.refreshToken eq token }.firstOrNull()?.apply { refreshToken = refresh }
            } ?: throw BadRequestException("Invalid token")
            val tokenPair = TokenPair(access, refresh)
            call.respond(HttpStatusCode.OK, tokenPair)
        }
    }
}