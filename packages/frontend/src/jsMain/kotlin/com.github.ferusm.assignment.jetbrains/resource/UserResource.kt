package com.github.ferusm.assignment.jetbrains.resource

import com.github.ferusm.assignment.jetbrains.model.Password
import com.github.ferusm.assignment.jetbrains.model.User
import com.github.ferusm.assignment.jetbrains.provider.HttpClientProvider
import io.ktor.client.request.*

object UserResource {
    const val USERS_MODULE_URL = "/users"

    suspend fun create(user: User): User = HttpClientProvider.reqular.post("${HttpClientProvider.BASIC_URL}${USERS_MODULE_URL}") {
        body = user
    }

    suspend fun read(): User = HttpClientProvider.authenticated.get("${HttpClientProvider.BASIC_URL}${USERS_MODULE_URL}")

    suspend fun update(password: Password): User = HttpClientProvider.authenticated.put("${HttpClientProvider.BASIC_URL}${USERS_MODULE_URL}/password") {
        body = password
    }
}