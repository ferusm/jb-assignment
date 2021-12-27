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
import io.ktor.client.statement.*
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
        HttpResponseValidator {
            handleResponseException { exception ->
                if (exception !is ClientRequestException) return@handleResponseException
                val response = exception.response
                window.alert("[${response.status}]: ${exception.response.readText()}")
            }
        }
    }

    fun authenticated(aTokens: TokenPair, onUnauthorized: () -> Unit, onTokenChange: (TokenPair) -> Unit): HttpClient {
        return HttpClient {
            install(Auth) {
                var tokens = BearerTokens(aTokens.access, aTokens.refresh)
                bearer {
                    loadTokens {
                        tokens = BearerTokens(aTokens.access, aTokens.refresh)
                        tokens
                    }
                    refreshTokens {
                        val newTokens = AuthResource.refresh(RefreshToken(tokens.refreshToken))
                        onTokenChange(newTokens)
                        tokens = BearerTokens(newTokens.access, newTokens.refresh)
                        tokens
                    }
                }
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
            HttpResponseValidator {
                handleResponseException { exception ->
                    if (exception !is ClientRequestException) return@handleResponseException
                    val response = exception.response
                    window.alert("[${response.status}]: ${exception.response.readText()}")
                    if (response.status == HttpStatusCode.Unauthorized) {
                        onUnauthorized()
                    }
                }
            }
        }
    }
}