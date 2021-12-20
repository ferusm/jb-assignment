package com.github.ferusm.assignment.jetbrains

import com.github.ferusm.assignment.jetbrains.model.*
import com.github.ferusm.assignment.jetbrains.module.auth
import com.github.ferusm.assignment.jetbrains.module.main
import com.github.ferusm.assignment.jetbrains.module.users
import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class UsersModuleTest {
    private val environment = createTestEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load("application-test.conf"))
        module {
            main()
            users()
            auth()
        }
    }

    @Test
    fun createUser() {
        withApplication(environment) {
            val userRequest = User("testUser13", "test", Role.USER)
            val userResponse = with(handleRequest(HttpMethod.Post, "/api/users") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(userRequest))
            }) {
                assertEquals(HttpStatusCode.Created, response.status())
                Json.decodeFromString<User>(response.content!!)
            }
            assertEquals(userRequest.name, userResponse.name)
            assertEquals(userRequest.role, userResponse.role)
        }
    }

    @Test
    fun createWithUsedName() {
        withApplication(environment) {
            val userRequest = User("testUser14", "test", Role.USER)
            with(handleRequest(HttpMethod.Post, "/api/users") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(userRequest))
            }) {
                assertEquals(HttpStatusCode.Created, response.status())
            }
            with(handleRequest(HttpMethod.Post, "/api/users") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(userRequest))
            }) {
                assertEquals(HttpStatusCode.Conflict, response.status())
            }
        }
    }

    @Test
    fun getCurrentUser() {
        withApplication(environment) {
            val userRequest = User("testUser15", "test", Role.USER)
            with(handleRequest(HttpMethod.Post, "/api/users") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(userRequest))
            }) {
                Json.decodeFromString<User>(response.content!!)
            }
            val tokenRequest = Credentials(userRequest.name, userRequest.identifier!!)
            val tokenResponse = with(handleRequest(HttpMethod.Post, "/api/tokens") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(tokenRequest))
            }) {
                Json.decodeFromString<TokenPair>(response.content!!)
            }
            val response = with(handleRequest(HttpMethod.Get, "/api/users/current") {
                addHeader(HttpHeaders.Authorization, "Bearer ${tokenResponse.access}")
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
                Json.decodeFromString<User>(response.content!!)
            }
            assertEquals(userRequest.name, response.name)
            assertEquals(userRequest.role, response.role)
        }
    }

    @Test
    fun getCurrentUserWithoutAuthentication() {
        withApplication(environment) {
            with(handleRequest(HttpMethod.Get, "/api/users/current") {
                addHeader(HttpHeaders.Authorization, "Bearer ${UUID.randomUUID()}")
            }) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun changePassword() {
        withApplication(environment) {
            val userRequest = User("testUser16", "test", Role.USER)
            with(handleRequest(HttpMethod.Post, "/api/users") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(userRequest))
            }) {
                Json.decodeFromString<User>(response.content!!)
            }
            var tokenRequest = Credentials(userRequest.name, userRequest.identifier!!)
            val tokenResponse = with(handleRequest(HttpMethod.Post, "/api/tokens") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(tokenRequest))
            }) {
                Json.decodeFromString<TokenPair>(response.content!!)
            }
            val password = Password("newPassword")
            with(handleRequest(HttpMethod.Put, "/api/users/current/password") {
                addHeader(HttpHeaders.Authorization, "Bearer ${tokenResponse.access}")
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(password))
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
            tokenRequest = Credentials(userRequest.name, password.password)
            with(handleRequest(HttpMethod.Post, "/api/tokens") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(tokenRequest))
            }) {
                assertEquals(HttpStatusCode.Created, response.status())
            }
        }
    }

    @Test
    fun changePasswordWithoutAuthentication() {
        withApplication(environment) {
            val password = Password("newPassword")
            with(handleRequest(HttpMethod.Put, "/api/users/current/password") {
                addHeader(HttpHeaders.Authorization, "Bearer ${UUID.randomUUID()}")
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(password))
            }) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }
}