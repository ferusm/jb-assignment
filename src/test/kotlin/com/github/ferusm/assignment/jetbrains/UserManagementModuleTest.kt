package com.github.ferusm.assignment.jetbrains

import at.favre.lib.crypto.bcrypt.BCrypt
import com.github.ferusm.assignment.jetbrains.model.Credentials
import com.github.ferusm.assignment.jetbrains.model.Role
import com.github.ferusm.assignment.jetbrains.model.Session
import com.github.ferusm.assignment.jetbrains.model.User
import com.github.ferusm.assignment.jetbrains.module.coreModule
import com.github.ferusm.assignment.jetbrains.module.userManagementModule
import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.*

class UserManagementModuleTest {
    private val environment = createTestEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load("application-test.conf"))
        module {
            coreModule()
            userManagementModule()
        }
    }

    @Test
    fun createUser() {
        withApplication(environment) {
            val userRequest = User("test", "test", Role.ADMIN)
            val userResponse = with(handleRequest(HttpMethod.Post, "/user") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(userRequest))
            }) {
                assertEquals(HttpStatusCode.Created, response.status())
                Json.decodeFromString<User>(response.content!!)
            }
            assertEquals(userResponse.username, userRequest.username)
            assertEquals(userResponse.role, userRequest.role)
            val isPasswordEncryptedProperly = BCrypt
                .verifyer()
                .verify(userRequest.password.toCharArray(), userResponse.password.toCharArray())
                .verified
            assert(isPasswordEncryptedProperly)
        }
    }

    @Test
    fun createSession() {
        withApplication(environment) {
            val userRequest = User("test", "test", Role.ADMIN)
            handleRequest(HttpMethod.Post, "/user") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(userRequest))
            }
            val credentialsRequest = Credentials("test", "test")
            val sessionResponse = with(handleRequest(HttpMethod.Post, "/session") {
                addHeader(HttpHeaders.ContentType, "${ContentType.Application.Json}")
                setBody(Json.encodeToString(credentialsRequest))
            }) {
                assertEquals(HttpStatusCode.Created, response.status())
                Json.decodeFromString<Session>(response.content!!)
            }
            assertEquals(userRequest.username, sessionResponse.username)
            assertEquals(userRequest.role, sessionResponse.role)
            assert(sessionResponse.token.isNotBlank())
        }
    }

    @Test
    fun changePasswordWithoutToken() {
        withApplication(environment) {
            with(handleRequest(HttpMethod.Put, "/password") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.contentType)
            }) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }
}