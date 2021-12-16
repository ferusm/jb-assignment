package com.github.ferusm.assignment.jetbrains


import com.github.ferusm.assignment.jetbrains.module.adminAreaModule
import com.github.ferusm.assignment.jetbrains.module.coreModule
import com.github.ferusm.assignment.jetbrains.module.reviewerAreaModule
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class AdminAreaModuleTest {
    private val testEnv = createTestEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load("application-test.conf"))
        module {
            coreModule()
            adminAreaModule()
        }
    }

    @Test
    fun getAllUsersWithoutToken() {
        withApplication(testEnv) {
            with(handleRequest(HttpMethod.Get, "/admin/users")) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }
}