package com.github.ferusm.assignment.jetbrains.model

enum class Role {
    ADMIN,
    REVIEWER,
    USER,
    UNKNOWN;

    fun check(role: Role) = ordinal >= role.ordinal
}