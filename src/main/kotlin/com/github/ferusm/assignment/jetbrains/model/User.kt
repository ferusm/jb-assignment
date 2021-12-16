package com.github.ferusm.assignment.jetbrains.model

import kotlinx.serialization.Serializable

@Serializable
data class User(val username: String, val password: String, val role: Role)