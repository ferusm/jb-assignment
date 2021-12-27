package com.github.ferusm.assignment.jetbrains

import com.github.ferusm.assignment.jetbrains.model.Credentials
import com.github.ferusm.assignment.jetbrains.model.RefreshToken
import com.github.ferusm.assignment.jetbrains.model.TokenPair
import com.github.ferusm.assignment.jetbrains.model.User
import com.github.ferusm.assignment.jetbrains.module.auth
import com.github.ferusm.assignment.jetbrains.module.main
import com.github.ferusm.assignment.jetbrains.module.users
import com.github.ferusm.assignment.jetbrains.role.UserRole
import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthModuleTest {
    private val environment = createTestEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load("application-test.conf"))
        module {
            main()
            users()
            auth()
        }
    }

    @Test
    fun createTokens() {
        withApplication(environment) {
            val userRequest = User("testUser1", "test", UserRole)
            handleRequest(HttpMethod.Post, "/api/users") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(userRequest))
            }
            val tokenRequest = Credentials(userRequest.name, userRequest.identifier!!)
            with(handleRequest(HttpMethod.Post, "/api/tokens") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(tokenRequest))
            }) {
                assertEquals(HttpStatusCode.Created, response.status())
            }
        }
    }

    @Test
    fun createTokensWithWrongCredentials() {
        withApplication(environment) {
            val tokenRequest = Credentials("123", "456")
            with(handleRequest(HttpMethod.Post, "/api/tokens") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(tokenRequest))
            }) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun accessTokenTimeout() {
        withApplication(environment) {
            runBlocking {
                val userRequest = User("testUser2", "test", UserRole)
                handleRequest(HttpMethod.Post, "/api/users") {
                    addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                    setBody(Json.encodeToString(userRequest))
                }
                val tokenRequest = Credentials(userRequest.name, userRequest.identifier!!)
                val tokenResponse = with(handleRequest(HttpMethod.Post, "/api/tokens") {
                    addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                    setBody(Json.encodeToString(tokenRequest))
                }) {
                    Json.decodeFromString<TokenPair>(response.content!!)
                }
                delay(2000)
                with(handleRequest(HttpMethod.Get, "/api/users/current") {
                    addHeader(HttpHeaders.Authorization, "Bearer ${tokenResponse.access}")
                }) {
                    assertEquals(HttpStatusCode.Unauthorized, response.status())
                }
            }
        }
    }

    @Test
    fun refreshToken() {
        withApplication(environment) {
            val userRequest = User("testUser3", "test", UserRole)
            handleRequest(HttpMethod.Post, "/api/users") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(userRequest))
            }
            val tokenRequest = Credentials(userRequest.name, userRequest.identifier!!)
            var tokenResponse = with(handleRequest(HttpMethod.Post, "/api/tokens") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(tokenRequest))
            }) {
                Json.decodeFromString<TokenPair>(response.content!!)
            }
            val refresh = RefreshToken(tokenResponse.refresh)
            tokenResponse = with(handleRequest(HttpMethod.Post, "/api/refresh") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(refresh))
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
                Json.decodeFromString(response.content!!)
            }
            with(handleRequest(HttpMethod.Get, "/api/users/current") {
                addHeader(HttpHeaders.Authorization, "Bearer ${tokenResponse.access}")
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun multipleUseOfRefreshToken() {
        withApplication(environment) {
            runBlocking {
                val userRequest = User("testUser4", "test", UserRole)
                handleRequest(HttpMethod.Post, "/api/users") {
                    addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                    setBody(Json.encodeToString(userRequest))
                }
                val tokenRequest = Credentials(userRequest.name, userRequest.identifier!!)
                val tokenResponse = with(handleRequest(HttpMethod.Post, "/api/tokens") {
                    addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                    setBody(Json.encodeToString(tokenRequest))
                }) {
                    Json.decodeFromString<TokenPair>(response.content!!)
                }
                delay(1000)
                val refresh = RefreshToken(tokenResponse.refresh)
                with(handleRequest(HttpMethod.Post, "/api/refresh") {
                    addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                    setBody(Json.encodeToString(refresh))
                }) {
                    assertEquals(HttpStatusCode.OK, response.status())
                    println(response.content)
                }
                with(handleRequest(HttpMethod.Post, "/api/refresh") {
                    addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                    setBody(Json.encodeToString(refresh))
                }) {
                    assertEquals(HttpStatusCode.Unauthorized, response.status())
                    println(response.content)
                }
            }
        }
    }

    @Test
    fun logout() {
        withApplication(environment) {
            val userRequest = User("testUser5", "test", UserRole)
            handleRequest(HttpMethod.Post, "/api/users") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(userRequest))
            }
            val tokenRequest = Credentials(userRequest.name, userRequest.identifier!!)
            val tokenResponse = with(handleRequest(HttpMethod.Post, "/api/tokens") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(tokenRequest))
            }) {
                Json.decodeFromString<TokenPair>(response.content!!)
            }
            with(handleRequest(HttpMethod.Post, "/api/logout") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(RefreshToken(tokenResponse.refresh)))
            }) {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun logoutWithoutAuthentication() {
        withApplication(environment) {
            with(handleRequest(HttpMethod.Post, "/api/logout") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(RefreshToken(UUID.randomUUID().toString())))
            }) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }
}