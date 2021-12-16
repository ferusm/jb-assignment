package com.github.ferusm.assignment.jetbrains

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class UserManagementModuleTest {
    @Test
    fun createUser() {
        withTestApplication(Application::userManagementModule) {
            with(handleRequest(HttpMethod.Post, "/user")) {
                assertEquals(HttpStatusCode.NotImplemented, response.status())
            }
        }
    }

    @Test
    fun createSession() {
        withTestApplication(Application::userManagementModule) {
            with(handleRequest(HttpMethod.Post, "/session")) {
                assertEquals(HttpStatusCode.NotImplemented, response.status())
            }
        }
    }

    @Test
    fun changePassword() {
        withTestApplication(Application::userManagementModule) {
            with(handleRequest(HttpMethod.Put, "/password")) {
                assertEquals(HttpStatusCode.NotImplemented, response.status())
            }
        }
    }
}