package com.github.ferusm.assignment.jetbrains.role

object ReviewerRole : Role() {
    override val name: String = "REVIEWER"
    override val includes: List<Role> = listOf(UserRole)
}