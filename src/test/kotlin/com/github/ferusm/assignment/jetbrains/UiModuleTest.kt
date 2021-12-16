package com.github.ferusm.assignment.jetbrains

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class UiModuleTest {
    @Test
    fun getLoginPage() {
        withTestApplication(Application::uiModule) {
            with(handleRequest(HttpMethod.Get, "/")) {
                assertEquals(HttpStatusCode.NotImplemented, response.status())
            }
        }
    }

    @Test
    fun getPaPage() {
        withTestApplication(Application::uiModule) {
            with(handleRequest(HttpMethod.Get, "/pa")) {
                assertEquals(HttpStatusCode.NotImplemented, response.status())
            }
        }
    }
}