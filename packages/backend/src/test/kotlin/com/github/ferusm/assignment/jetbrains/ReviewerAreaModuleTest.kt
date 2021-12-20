package com.github.ferusm.assignment.jetbrains

import com.github.ferusm.assignment.jetbrains.model.Credentials
import com.github.ferusm.assignment.jetbrains.model.Role
import com.github.ferusm.assignment.jetbrains.model.TokenPair
import com.github.ferusm.assignment.jetbrains.model.User
import com.github.ferusm.assignment.jetbrains.module.auth
import com.github.ferusm.assignment.jetbrains.module.main
import com.github.ferusm.assignment.jetbrains.module.reviewerArea
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

class ReviewerAreaModuleTest {
    private val environment = createTestEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load("application-test.conf"))
        module {
            main()
            users()
            auth()
            reviewerArea()
        }
    }

    @Test
    fun getWithAuthentication() {
        withApplication(environment) {
            val userRequest = User("testReviewer", "test", Role.REVIEWER)
            val userResponse = with(handleRequest(HttpMethod.Post, "/api/users") {
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
            val response = with(handleRequest(HttpMethod.Get, "/api/reviewer/hello") {
                addHeader(HttpHeaders.Authorization, "Bearer ${tokenResponse.access}")
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content!!
            }
            assertEquals(response, "You’re a ${Role.REVIEWER}, ${userResponse.name}")
        }
    }

    @Test
    fun getWithAdminRole() {
        withApplication(environment) {
            val userRequest = User("testAdmin", "test", Role.ADMIN)
            val userResponse = with(handleRequest(HttpMethod.Post, "/api/users") {
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
            val response = with(handleRequest(HttpMethod.Get, "/api/reviewer/hello") {
                addHeader(HttpHeaders.Authorization, "Bearer ${tokenResponse.access}")
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content!!
            }
            assertEquals(response, "You’re a ${Role.REVIEWER}, ${userResponse.name}")
        }
    }

    @Test
    fun getWithWrongRole() {
        withApplication(environment) {
            val userRequest = User("testUser10", "test", Role.USER)
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
            with(handleRequest(HttpMethod.Get, "/api/reviewer/hello") {
                addHeader(HttpHeaders.Authorization, "Bearer ${tokenResponse.access}")
            }) {
                assertEquals(HttpStatusCode.Forbidden, response.status())
            }
        }
    }

    @Test
    fun getWithoutAuthentication() {
        withApplication(environment) {
            with(handleRequest(HttpMethod.Get, "/api/reviewer/hello") {
                addHeader(HttpHeaders.Authorization, "Bearer ${UUID.randomUUID()}")
            }) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }
}