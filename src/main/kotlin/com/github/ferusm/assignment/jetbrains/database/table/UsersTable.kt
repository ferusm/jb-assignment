package com.github.ferusm.assignment.jetbrains.database.table

import com.github.ferusm.assignment.jetbrains.model.Role
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object UsersTable: IntIdTable("Users") {
    val name: Column<String> = varchar("name", 20).uniqueIndex()
    val password: Column<String> = varchar("password", 80)
    val role: Column<Role> = enumerationByName("role", 10, Role::class)
}