package com.github.ferusm.assignment.jetbrains.util

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.github.ferusm.assignment.jetbrains.feature.RoleBasedAuthorization
import com.github.ferusm.assignment.jetbrains.model.User
import com.github.ferusm.assignment.jetbrains.role.Role
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import java.util.*
import kotlin.reflect.KClass
import kotlin.time.Duration

object JWTUtil {
    private const val NAME_CLAIM = "name"
    private const val ROLE_CLAIM = "role"

    fun generateAccessToken(
        username: String,
        role: String,
        config: JWTConfig
    ): String = generateToken(config, config.accessTokenTtl, NAME_CLAIM to username, ROLE_CLAIM to role)

    fun generateRefreshToken(config: JWTConfig): String = generateToken(config, config.refreshTokenTtl)

    private fun generateToken(config: JWTConfig, ttl: Duration, vararg claims: Pair<String, String>): String {
        val builder = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date.from(Clock.System.now().plus(ttl).toJavaInstant()))
        claims.forEach { (name, value) ->
            builder.withClaim(name, value)
        }
        return builder.sign(Algorithm.HMAC256(config.secret))
    }

    fun isValidToken(token: String, config: JWTConfig): Boolean {
        val verifier = createVerifier(config)
        val result = runCatching {
            verifier.verify(token)
        }
        return result.isSuccess
    }

    private fun createVerifier(config: JWTConfig): JWTVerifier = JWT
        .require(Algorithm.HMAC256(config.secret))
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .build()

    fun Application.installJwtAuthentication(config: JWTConfig) {
        val verifier = createVerifier(config)
        install(Authentication) {
            jwt {
                realm = config.realm
                verifier(verifier)
                validate { jwtCredential -> JWTPrincipal(jwtCredential.payload) }
            }
        }
    }

    fun Application.installClaimBasedRoleAuthorization() {
        install(RoleBasedAuthorization) {
            roleProvider = { principal ->
                if (principal is JWTPrincipal) {
                    val roleName = principal.getClaimOrThrow(ROLE_CLAIM, String::class)
                    Role.get(roleName)
                } else {
                    throw IllegalArgumentException("Authorization principal must be JWTPrincipal")
                }
            }
        }
    }

    fun AuthenticationContext.user(): User {
        val principal =
            principal<JWTPrincipal>() ?: throw IllegalArgumentException("Unable to retrieve JWTPrincipal from call")
        val userName = principal.getClaimOrThrow(NAME_CLAIM, String::class)
        val roleName = principal.getClaimOrThrow(ROLE_CLAIM, String::class)
        val role = Role.get(roleName)
        return User(userName, null, role)
    }
}

fun <T : Any> JWTPrincipal.getClaimOrThrow(name: String, clazz: KClass<T>): T {
    return getClaim(name, clazz) ?: throw IllegalArgumentException("JWTPrincipal claim '$name' is null")
}