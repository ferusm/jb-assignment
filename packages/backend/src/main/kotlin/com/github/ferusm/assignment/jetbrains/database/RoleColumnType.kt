package com.github.ferusm.assignment.jetbrains.database

import com.github.ferusm.assignment.jetbrains.role.Role
import org.jetbrains.exposed.sql.StringColumnType

class RoleColumnType(collate: String? = null) : StringColumnType(collate) {
    override fun sqlType(): String = "VARCHAR(20)"

    override fun valueFromDB(value: Any): Any {
        return when (value) {
            is String -> Role.get(value)
            else -> value
        }
    }

    override fun valueToDB(value: Any?): Any? {
        val role = value as Role?
        val roleName = role?.name
        return super.valueToDB(roleName)
    }

    override fun valueToString(value: Any?): String {
        val role = value as Role?
        val roleName = role?.name
        return super.valueToString(roleName)
    }
}