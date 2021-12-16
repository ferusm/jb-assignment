package com.github.ferusm.assignment.jetbrains

import com.github.ferusm.assignment.jetbrains.module.coreModule
import com.github.ferusm.assignment.jetbrains.module.userManagementModule
import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class UserManagementModuleTest {
    private val testEnv = createTestEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load("application-test.conf"))
        module {
            coreModule()
            userManagementModule()
        }
    }

    @Test
    fun createUser() {
        withApplication(testEnv) {
            with(handleRequest(HttpMethod.Post, "/user") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.contentType)
            }) {
                assertEquals(HttpStatusCode.NotImplemented, response.status())
            }
        }
    }

    @Test
    fun createSessionWithoutCredentials() {
        withApplication(testEnv) {
            with(handleRequest(HttpMethod.Post, "/session") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.contentType)
            }) {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }

    @Test
    fun changePasswordWithoutToken() {
        withApplication(testEnv) {
            with(handleRequest(HttpMethod.Put, "/password") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.contentType)
            }) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }
}