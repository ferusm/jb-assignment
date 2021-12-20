package com.github.ferusm.assignment.jetbrains.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.github.ferusm.assignment.jetbrains.feature.RoleBasedAuthorization
import com.github.ferusm.assignment.jetbrains.model.Role
import com.github.ferusm.assignment.jetbrains.model.User
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.config.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import java.util.*
import kotlin.time.Duration

object JWTUtil {
    fun generateAccessToken(
        username: String,
        role: Role,
        config: ApplicationConfig
    ): String {
        val secret = config.property("ktor.jwt.secret").getString()
        val issuer = config.property("ktor.jwt.issuer").getString()
        val audience = config.property("ktor.jwt.audience").getString()
        val ttl = Duration.parse(config.property("ktor.jwt.accessTtl").getString())

        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("name", username)
            .withClaim("role", role.name)
            .withExpiresAt(Date.from(Clock.System.now().plus(ttl).toJavaInstant()))
            .sign(Algorithm.HMAC256(secret))
    }

    fun generateRefreshToken(config: ApplicationConfig): String {
        val secret = config.property("ktor.jwt.secret").getString()
        val issuer = config.property("ktor.jwt.issuer").getString()
        val audience = config.property("ktor.jwt.audience").getString()
        val ttl = Duration.parse(config.property("ktor.jwt.refreshTtl").getString())

        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withExpiresAt(Date.from(Clock.System.now().plus(ttl).toJavaInstant()))
            .sign(Algorithm.HMAC256(secret))
    }

    fun isValidRefreshToken(token: String, config: ApplicationConfig): Boolean {
        val secret = config.property("ktor.jwt.secret").getString()
        val issuer = config.property("ktor.jwt.issuer").getString()
        val audience = config.property("ktor.jwt.audience").getString()
        val verifier = JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
        val result = runCatching {
            verifier.verify(token)
        }
        return result.isSuccess
    }

    fun Application.installJwtAuthentication() {
        val config = environment.config
        val secret = config.property("ktor.jwt.secret").getString()
        val issuer = config.property("ktor.jwt.issuer").getString()
        val audience = config.property("ktor.jwt.audience").getString()
        val jwtRealm = config.property("ktor.jwt.realm").getString()

        val jwtVerifier = JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()

        install(Authentication) {
            jwt {
                realm = jwtRealm
                verifier(jwtVerifier)
                validate { jwtCredential -> JWTPrincipal(jwtCredential.payload) }
            }
        }
    }

    fun Application.installClaimBasedRoleAuthorization() {
        install(RoleBasedAuthorization) {
            roleProvider = { principal ->
                if (principal is JWTPrincipal) {
                    val role = principal.getClaim("role", String::class)
                    Role.values().find { it.name == role }
                } else {
                    null
                }
            }
        }
    }

    fun AuthenticationContext.user(): User {
        val principal = principal<JWTPrincipal>()!!
        val username = principal.getClaim("name", String::class)!!
        val role = principal.getClaim("role", String::class)?.let { role ->
            Role.values().find { it.name == role }
        }!!
        return User(username, null, role)
    }
}