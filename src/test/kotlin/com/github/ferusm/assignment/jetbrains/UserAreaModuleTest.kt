package com.github.ferusm.assignment.jetbrains

import com.github.ferusm.assignment.jetbrains.module.coreModule
import com.github.ferusm.assignment.jetbrains.module.userAreaModule
import com.github.ferusm.assignment.jetbrains.module.userManagementModule
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class UserAreaModuleTest {
    private val testEnv = createTestEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load("application-test.conf"))
        module {
            coreModule()
            userAreaModule()
        }
    }

    @Test
    fun getCurrentSessionWithoutToken() {
        withApplication(testEnv) {
            with(handleRequest(HttpMethod.Get, "/user/session")) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }
}