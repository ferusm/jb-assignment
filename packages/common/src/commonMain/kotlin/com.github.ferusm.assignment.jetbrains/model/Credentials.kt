package com.github.ferusm.assignment.jetbrains.model

import kotlinx.serialization.Serializable

@Serializable
data class Credentials(val name: String, val identifier: String)