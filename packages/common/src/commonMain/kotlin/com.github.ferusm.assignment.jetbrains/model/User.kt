package com.github.ferusm.assignment.jetbrains.model

import kotlinx.serialization.Serializable

@Serializable
data class User(val name: String, val identifier: String, val role: Role)