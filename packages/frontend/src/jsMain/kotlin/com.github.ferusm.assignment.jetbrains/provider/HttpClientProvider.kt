package com.github.ferusm.assignment.jetbrains.provider


import com.github.ferusm.assignment.jetbrains.delegate.TokensDelegate
import com.github.ferusm.assignment.jetbrains.model.RefreshToken
import com.github.ferusm.assignment.jetbrains.resource.TokensResource
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import kotlinx.browser.window

object HttpClientProvider {
    val BASIC_URL = "${window.origin}/api"

    val reqular = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }

    val authenticated = HttpClient {
        install(Auth) {
            bearer {
                loadTokens {
                    val tokenPair by TokensDelegate
                    tokenPair?.run { BearerTokens(access, refresh) }
                }
                refreshTokens {
                    val oldTokens by TokensDelegate
                    oldTokens?.run {
                        val newTokens = TokensResource.refresh(RefreshToken(refresh))
                        BearerTokens(newTokens.access, newTokens.refresh)
                    }
                }
            }
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }
}