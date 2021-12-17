package com.github.ferusm.assignment.jetbrains.util

import at.favre.lib.crypto.bcrypt.BCrypt

object BCryptUtil {
    private val encryptor = BCrypt.withDefaults()
    private val verifier = BCrypt.verifyer()

    fun encrypt(cost: Int, input: String): String = encryptor.hashToString(cost, input.toCharArray())
    fun isValid(hash: String, input: String): Boolean = verifier.verify(input.toCharArray(), hash.toCharArray()).verified
}