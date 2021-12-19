package com.github.ferusm.assignment.jetbrains

import org.jetbrains.compose.web.renderComposable


fun main() { renderComposable(rootElementId = "root") { Application() } }