package com.github.ferusm.assignment.jetbrains

import com.github.ferusm.assignment.jetbrains.module.coreModule
import com.github.ferusm.assignment.jetbrains.module.uiModule
import com.github.ferusm.assignment.jetbrains.module.userAreaModule
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class UiModuleTest {
    private val testEnv = createTestEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load("application-test.conf"))
        module {
            coreModule()
            uiModule()
        }
    }

    @Test
    fun getLoginPage() {
        withApplication(testEnv) {
            with(handleRequest(HttpMethod.Get, "/")) {
                assertEquals(HttpStatusCode.NotImplemented, response.status())
            }
        }
    }

    @Test
    fun getPaPageWithoutToken() {
        withApplication(testEnv) {
            with(handleRequest(HttpMethod.Get, "/pa")) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }
}