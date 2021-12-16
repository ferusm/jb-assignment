package com.github.ferusm.assignment.jetbrains


import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class AdminAreaModuleTest {
    @Test
    fun getAllUsers() {
        withTestApplication(Application::adminAreaModule) {
            with(handleRequest(HttpMethod.Get, "/admin/users")) {
                assertEquals(HttpStatusCode.NotImplemented, response.status())
            }
        }
    }
}