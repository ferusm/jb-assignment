package com.github.ferusm.assignment.jetbrains.role

object AdminRole: Role() {
    override val name: String = "ADMIN"
    override val includes: List<Role> = listOf(ReviewerRole)
}