package com.github.ferusm.assignment.jetbrains

import com.github.ferusm.assignment.jetbrains.model.Credentials
import com.github.ferusm.assignment.jetbrains.model.TokenPair
import com.github.ferusm.assignment.jetbrains.model.User
import com.github.ferusm.assignment.jetbrains.module.auth
import com.github.ferusm.assignment.jetbrains.module.main
import com.github.ferusm.assignment.jetbrains.module.userArea
import com.github.ferusm.assignment.jetbrains.module.users
import com.github.ferusm.assignment.jetbrains.role.AdminRole
import com.github.ferusm.assignment.jetbrains.role.ReviewerRole
import com.github.ferusm.assignment.jetbrains.role.UserRole
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

class UserAreaModuleTest {
    private val environment = createTestEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load("application-test.conf"))
        module {
            main()
            users()
            auth()
            userArea()
        }
    }

    @Test
    fun getWithAuthentication() {
        withApplication(environment) {
            val userRequest = User("testUser11", "test", UserRole)
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
            val response = with(handleRequest(HttpMethod.Get, "/api/user/hello") {
                addHeader(HttpHeaders.Authorization, "Bearer ${tokenResponse.access}")
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content!!
            }
            assertEquals(response, "You???re a ${UserRole.name}, ${userResponse.name}")
        }
    }

    @Test
    fun getWithReviewerRole() {
        withApplication(environment) {
            val userRequest = User("testReviewer2", "test", ReviewerRole)
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
            val response = with(handleRequest(HttpMethod.Get, "/api/user/hello") {
                addHeader(HttpHeaders.Authorization, "Bearer ${tokenResponse.access}")
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content!!
            }
            assertEquals(response, "You???re a ${UserRole.name}, ${userResponse.name}")
        }
    }

    @Test
    fun getWithAdminRole() {
        withApplication(environment) {
            val userRequest = User("testAdmin2", "test", AdminRole)
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
            val response = with(handleRequest(HttpMethod.Get, "/api/user/hello") {
                addHeader(HttpHeaders.Authorization, "Bearer ${tokenResponse.access}")
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
                response.content!!
            }
            assertEquals(response, "You???re a ${UserRole.name}, ${userResponse.name}")
        }
    }

    @Test
    fun getWithoutAuthentication() {
        withApplication(environment) {
            with(handleRequest(HttpMethod.Get, "/api/user/hello") {
                addHeader(HttpHeaders.Authorization, "Bearer ${UUID.randomUUID()}")
            }) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }
}