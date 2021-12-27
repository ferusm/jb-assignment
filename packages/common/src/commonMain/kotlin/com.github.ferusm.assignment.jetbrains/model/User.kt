package com.github.ferusm.assignment.jetbrains.model

import com.github.ferusm.assignment.jetbrains.role.Role
import com.github.ferusm.assignment.jetbrains.serializer.RoleSerializer
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val identifier: String? = null,
    @Serializable(with = RoleSerializer::class) val role: Role
)