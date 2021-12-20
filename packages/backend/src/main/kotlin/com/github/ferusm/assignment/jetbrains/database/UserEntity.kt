package com.github.ferusm.assignment.jetbrains.database


import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable)

    var name by UsersTable.name
    var identifier by UsersTable.identifier
    var role by UsersTable.role
    var refreshToken by UsersTable.refreshToken
}