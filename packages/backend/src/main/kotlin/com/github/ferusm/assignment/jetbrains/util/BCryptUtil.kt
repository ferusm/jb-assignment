package com.github.ferusm.assignment.jetbrains.util

import at.favre.lib.crypto.bcrypt.BCrypt

object BCryptUtil {
    private val encryptor = BCrypt.withDefaults()
    private val verifier = BCrypt.verifyer()

    private const val DEFAULT_COST = 4

    fun encrypt(cost: Int, input: String): String = encryptor.hashToString(cost, input.toCharArray())
    fun encrypt(input: String): String = encrypt(DEFAULT_COST, input)

    fun isValid(hash: String, input: String): Boolean =
        verifier.verify(input.toCharArray(), hash.toCharArray()).verified
}