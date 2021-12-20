package com.github.ferusm.assignment.jetbrains.model

enum class Role {
    ADMIN,
    REVIEWER,
    USER;

    fun check(role: Role) = ordinal <= role.ordinal
}