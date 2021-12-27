package com.github.ferusm.assignment.jetbrains

import io.ktor.client.utils.*
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlin.random.Random

object Util {
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    fun randomString(length: Int): String = (1..length)
        .map { Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("");

    val context = newFixedThreadPoolContext(10, "TestExecutorContext")
}