package com.github.ferusm.assignment.jetbrains.resource

import com.github.ferusm.assignment.jetbrains.model.Credentials
import com.github.ferusm.assignment.jetbrains.model.RefreshToken
import com.github.ferusm.assignment.jetbrains.model.TokenPair
import com.github.ferusm.assignment.jetbrains.provider.HttpClientProvider
import io.ktor.client.request.*
import io.ktor.http.*

object TokensResource {
    const val TOKENS_MODULE_URL = "/auth"

    suspend fun create(credentials: Credentials): TokenPair = HttpClientProvider.reqular.post("${HttpClientProvider.BASIC_URL}${TOKENS_MODULE_URL}/tokens") {
        body = credentials
    }

    suspend fun refresh(token: RefreshToken): TokenPair = HttpClientProvider.reqular.post("${HttpClientProvider.BASIC_URL}${TOKENS_MODULE_URL}/refresh") {
        body = token
    }
}