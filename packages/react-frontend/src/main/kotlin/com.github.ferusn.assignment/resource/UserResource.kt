package com.github.ferusn.assignment.resource

import com.github.ferusm.assignment.jetbrains.model.Password
import com.github.ferusm.assignment.jetbrains.model.User
import com.github.ferusn.assignment.provider.HttpClientProvider
import io.ktor.client.*
import io.ktor.client.request.*

object UserResource {
    const val USERS_MODULE_URL = "/users"

    suspend fun create(user: User, client: HttpClient): User =
        client.post("${HttpClientProvider.BASIC_URL}$USERS_MODULE_URL") {
            body = user
        }

    suspend fun read(client: HttpClient): User = client.get("${HttpClientProvider.BASIC_URL}$USERS_MODULE_URL/current")

    suspend fun update(password: Password, client: HttpClient): Unit =
        client.put("${HttpClientProvider.BASIC_URL}$USERS_MODULE_URL/current/password") {
            body = password
        }
}