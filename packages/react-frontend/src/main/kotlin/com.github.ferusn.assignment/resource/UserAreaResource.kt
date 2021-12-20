package com.github.ferusn.assignment.resource


import com.github.ferusn.assignment.provider.HttpClientProvider
import io.ktor.client.*
import io.ktor.client.request.*

object UserAreaResource {
    const val USER_AREA_MODULE_URL = "/user"

    suspend fun hello(client: HttpClient): String = client.get("${HttpClientProvider.BASIC_URL}$USER_AREA_MODULE_URL/hello")
}