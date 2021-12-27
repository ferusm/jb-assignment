package com.github.ferusm.assignment.jetbrains.feature

import com.github.ferusm.assignment.jetbrains.role.Role
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*

class RoleBasedAuthorization(private val config: Config) {
    data class Config(var roleProvider: (Principal) -> Role = { throw IllegalStateException("Default provider implementation must be replaced. Check your RoleBasedAuthorization installation code") })

    fun interceptPipeline(
        pipeline: ApplicationCallPipeline,
        role: Role
    ) {
        pipeline.insertPhaseAfter(ApplicationCallPipeline.Features, Authentication.ChallengePhase)
        pipeline.insertPhaseAfter(Authentication.ChallengePhase, AuthorizationPhase)

        pipeline.intercept(AuthorizationPhase) {
            val principal = call.authentication.principal<Principal>()
                ?: throw IllegalArgumentException("RoleBasedAuthorization feature can't retrieve Principal from call")
            val currentRole = config.roleProvider.invoke(principal)
            if (!currentRole.isHaveRights(role)) {
                call.respond(HttpStatusCode.Forbidden)
                finish()
            }
        }
    }

    companion object Feature : ApplicationFeature<ApplicationCallPipeline, Config, RoleBasedAuthorization> {
        val AuthorizationPhase = PipelinePhase("Authorization")

        override val key = AttributeKey<RoleBasedAuthorization>("RoleBasedAuthorization")

        override fun install(pipeline: ApplicationCallPipeline, configure: Config.() -> Unit): RoleBasedAuthorization {
            val config = Config().apply(configure)
            return RoleBasedAuthorization(config)
        }
    }
}

class AuthorizationRouteSelector(private val role: Role) : RouteSelector() {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation {
        return RouteSelectorEvaluation(true, RouteSelectorEvaluation.qualityTransparent)
    }

    override fun toString(): String = "(authorize: ${role.name}})"
}

fun Route.withRole(role: Role, build: Route.() -> Unit): Route {
    val authorizedRoute = createChild(AuthorizationRouteSelector(role))
    application.feature(RoleBasedAuthorization).interceptPipeline(authorizedRoute, role)
    authorizedRoute.build()
    return authorizedRoute
}