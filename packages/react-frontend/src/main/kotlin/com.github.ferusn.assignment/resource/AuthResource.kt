package com.github.ferusn.assignment.resource

import com.github.ferusm.assignment.jetbrains.model.Credentials
import com.github.ferusm.assignment.jetbrains.model.RefreshToken
import com.github.ferusm.assignment.jetbrains.model.TokenPair
import com.github.ferusn.assignment.provider.HttpClientProvider
import io.ktor.client.request.*

object AuthResource {
    private val client = HttpClientProvider.regular

    suspend fun create(credentials: Credentials): TokenPair = client.post("${HttpClientProvider.BASIC_URL}/tokens") {
        body = credentials
    }

    suspend fun refresh(token: RefreshToken): TokenPair = client.post("${HttpClientProvider.BASIC_URL}/refresh") {
        body = token
    }

    suspend fun logout(token: RefreshToken) = client.post<Unit>("${HttpClientProvider.BASIC_URL}/logout") {
        body = token
    }
}