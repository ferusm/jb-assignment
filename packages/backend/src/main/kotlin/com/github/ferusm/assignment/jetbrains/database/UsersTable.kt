package com.github.ferusm.assignment.jetbrains.database

import com.github.ferusm.assignment.jetbrains.role.Role
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column


object UsersTable : IntIdTable("users") {
    val name: Column<String> = varchar("name", 20).uniqueIndex()
    val identifier: Column<String> = varchar("password", 80)
    val role: Column<Role> = registerColumn("role", RoleColumnType())
    val refreshToken: Column<String> = varchar("refresh_token", 200).default("").index()
}