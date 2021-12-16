package com.github.ferusm.assignment.jetbrains

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class UserAreaModuleTest {
    @Test
    fun getCurrentSession() {
        withTestApplication(Application::userAreaModule) {
            with(handleRequest(HttpMethod.Get, "/user/session")) {
                assertEquals(HttpStatusCode.NotImplemented, response.status())
            }
        }
    }
}