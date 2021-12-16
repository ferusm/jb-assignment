package com.github.ferusm.assignment.jetbrains

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ReviewerAreaModuleTest {
    @Test
    fun getAllSessions() {
        withTestApplication(Application::reviewerAreaModule) {
            with(handleRequest(HttpMethod.Get, "/reviewer/metrics")) {
                assertEquals(HttpStatusCode.NotImplemented, response.status())
            }
        }
    }
}