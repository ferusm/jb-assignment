package com.github.ferusn.assignment.provider


import com.github.ferusm.assignment.jetbrains.model.RefreshToken
import com.github.ferusm.assignment.jetbrains.model.TokenPair
import com.github.ferusn.assignment.resource.AuthResource
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

    val regular = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }

    fun authenticated(aTokens: TokenPair, onTokenChange: (TokenPair) -> Unit): HttpClient {
        return HttpClient {
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(aTokens.access, aTokens.refresh)
                    }
                    refreshTokens {
                        val refresh = aTokens.refresh
                        val newTokens = AuthResource.refresh(RefreshToken(refresh))
                        onTokenChange(newTokens)
                        BearerTokens(newTokens.access, newTokens.refresh)
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
}