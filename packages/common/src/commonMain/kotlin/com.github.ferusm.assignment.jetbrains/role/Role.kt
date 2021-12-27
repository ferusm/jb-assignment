package com.github.ferusm.assignment.jetbrains.role

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class Role() {
    abstract val name: String
    open val includes: List<Role> = emptyList()

    private fun register() {
        add(this)
    }

    init {
        register()
    }

    companion object {
        private val roles: MutableMap<String, Role> = mutableMapOf()
        private val mutex: Mutex = Mutex(false)

        fun add(role: Role) = CoroutineScope(Dispatchers.Default).launch {
            mutex.withLock {
                roles[role.name] = role
            }
        }

        fun get(name: String): Role = roles[name] ?: throw IllegalArgumentException("Role with name: $name not found")
        fun get(): List<Role> = roles.values.toList()
    }

    fun isHaveRights(role: Role): Boolean {
        if (role == this) {
            return true
        }
        return includes.any { it.isHaveRights(role) }
    }
}