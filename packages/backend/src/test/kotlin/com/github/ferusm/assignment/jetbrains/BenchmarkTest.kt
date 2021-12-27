package com.github.ferusm.assignment.jetbrains

import com.github.ferusm.assignment.jetbrains.model.Credentials
import com.github.ferusm.assignment.jetbrains.model.Password
import com.github.ferusm.assignment.jetbrains.model.TokenPair
import com.github.ferusm.assignment.jetbrains.model.User
import com.github.ferusm.assignment.jetbrains.module.*
import com.github.ferusm.assignment.jetbrains.role.AdminRole
import com.github.ferusm.assignment.jetbrains.role.ReviewerRole
import com.github.ferusm.assignment.jetbrains.role.UserRole
import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

@ObsoleteCoroutinesApi
class BenchmarkTest {
    private val environment = createTestEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load("application-test.conf"))
        module {
            main()
            users()
            auth()
            adminArea()
            reviewerArea()
            userArea()
        }
    }

    private val roles = setOf(AdminRole, ReviewerRole, UserRole)
    private val defaultPassword = "PEPEGA"
    private val endpoints = mapOf(
        UserRole to "/api/user/hello",
        ReviewerRole to "/api/reviewer/hello",
        AdminRole to "/api/admin/hello"
    )

    @Test
    fun multithreadedTest() {
        withApplication(environment) {
            val users = mutableListOf<User>()
            val userCreateActor = actor<User> {
                for(message in channel) {
                    users.add(message)
                }
            }
            runBlocking {
                CoroutineScope(Util.context).async {
                    repeat(1000) {
                        launch {
                            val userRequest = User(Util.randomString(20), defaultPassword, roles.random())
                            val userResponse = with(handleRequest(HttpMethod.Post, "/api/users") {
                                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                                setBody(Json.encodeToString(userRequest))
                            }) {
                                assertEquals(HttpStatusCode.Created, response.status())
                                Json.decodeFromString<User>(response.content!!)
                            }
                            assertEquals(userRequest.name, userResponse.name)
                            assertEquals(userRequest.role, userResponse.role)
                            userCreateActor.send(userRequest)
                        }
                    }
                    repeat(1000) {
                        launch {
                            val user = users.random()
                            val (role, endpoint) = endpoints.entries.random()

                            val tokenRequest = Credentials(user.name, user.identifier!!)
                            val tokenResponse = with(handleRequest(HttpMethod.Post, "/api/tokens") {
                                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                                setBody(Json.encodeToString(tokenRequest))
                            }) {
                                response
                            }
                            if (tokenResponse.status() == HttpStatusCode.Created) {
                                val tokenPair = Json.decodeFromString<TokenPair>(tokenResponse.content!!)
                                with(handleRequest(HttpMethod.Get, endpoint) {
                                    addHeader(HttpHeaders.Authorization, "Bearer ${tokenPair.access}")
                                }) {
                                    if (user.role.isHaveRights(role)) {
                                        assertEquals(HttpStatusCode.OK, response.status())
                                    } else {
                                        assertEquals(HttpStatusCode.Forbidden, response.status())
                                    }
                                }
                            }

                        }
                    }
                    repeat(1000) {
                        launch {
                            val user = users.random()

                            val tokenRequest = Credentials(user.name, user.identifier!!)
                            val tokenResponse = with(handleRequest(HttpMethod.Post, "/api/tokens") {
                                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                                setBody(Json.encodeToString(tokenRequest))
                            }) {
                                response
                            }
                            if (tokenResponse.status() == HttpStatusCode.Created) {
                                val tokenPair = Json.decodeFromString<TokenPair>(tokenResponse.content!!)
                                val password = Password(Util.randomString(20))
                                with(handleRequest(HttpMethod.Put, "/api/users/current/password") {
                                    addHeader(HttpHeaders.Authorization, "Bearer ${tokenPair.access}")
                                    addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                                    setBody(Json.encodeToString(password))
                                }) {
                                    assertEquals(HttpStatusCode.OK, response.status())
                                }
                            }
                        }
                    }
                }.await()
            }
        }
    }
}