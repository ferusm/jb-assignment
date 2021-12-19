package com.github.ferusm.assignment.jetbrains.delegate

import com.github.ferusm.assignment.jetbrains.model.TokenPair
import kotlin.reflect.KProperty

object TokensDelegate {
    private var tokenPair: TokenPair? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): TokenPair? = tokenPair

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: TokenPair) {
        tokenPair = value
    }
}