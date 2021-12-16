package com.github.ferusm.assignment.jetbrains

import com.github.ferusm.assignment.jetbrains.module.coreModule
import com.github.ferusm.assignment.jetbrains.module.reviewerAreaModule
import com.github.ferusm.assignment.jetbrains.module.uiModule
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ReviewerAreaModuleTest {
    private val testEnv = createTestEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load("application-test.conf"))
        module {
            coreModule()
            reviewerAreaModule()
        }
    }

    @Test
    fun getAllSessionsWithoutToken() {
        withApplication(testEnv) {
            with(handleRequest(HttpMethod.Get, "/reviewer/metrics")) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }
}