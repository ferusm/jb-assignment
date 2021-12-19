package com.github.ferusm.assignment.jetbrains.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshToken(val refresh: String)