package com.github.ferusm.assignment.jetbrains.database

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.exposed.sql.Database

object DatabaseProvider {
    private val databases: MutableMap<String, Database> = mutableMapOf()
    private val mutex = Mutex(false)
    suspend fun get(url: String, driver: String): Database = mutex.withLock {
        databases.computeIfAbsent("$url-$driver") {
            Database.connect(url, driver)
        }
    }
}