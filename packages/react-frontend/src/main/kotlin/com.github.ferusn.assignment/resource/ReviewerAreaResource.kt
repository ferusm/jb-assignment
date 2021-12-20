package com.github.ferusn.assignment.resource

import com.github.ferusm.assignment.jetbrains.model.User
import com.github.ferusn.assignment.provider.HttpClientProvider
import io.ktor.client.*
import io.ktor.client.request.*

object ReviewerAreaResource{
    const val REVIEWER_AREA_MODULE_URL = "/reviewer"

    suspend fun hello(client: HttpClient): String = client.get("${HttpClientProvider.BASIC_URL}$REVIEWER_AREA_MODULE_URL/hello")
}