package com.github.ferusm.assignment.jetbrains.database

import com.github.ferusm.assignment.jetbrains.model.Role
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object UsersTable: IntIdTable("users") {
    val name: Column<String> = varchar("name", 20).uniqueIndex()
    val identifier: Column<String> = varchar("password", 80)
    val role: Column<Role> = enumerationByName("role", 10, Role::class)
    val refreshToken: Column<String> = varchar("refresh_token", 200).default("").index()
}