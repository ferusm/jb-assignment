package com.github.ferusm.assignment.jetbrains.database.entity


import com.github.ferusm.assignment.jetbrains.database.table.UsersTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<UserEntity>(UsersTable)

    var name by UsersTable.name
    var password by UsersTable.password
    var role by UsersTable.role
}