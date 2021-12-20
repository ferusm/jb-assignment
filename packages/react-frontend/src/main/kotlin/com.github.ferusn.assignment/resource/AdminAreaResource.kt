package com.github.ferusn.assignment.resource

import com.github.ferusn.assignment.provider.HttpClientProvider
import io.ktor.client.*
import io.ktor.client.request.*

object AdminAreaResource {
    const val ADMIN_AREA_MODULE_URL = "/admin"

    suspend fun hello(client: HttpClient): String =
        client.get("${HttpClientProvider.BASIC_URL}$ADMIN_AREA_MODULE_URL/hello")
}