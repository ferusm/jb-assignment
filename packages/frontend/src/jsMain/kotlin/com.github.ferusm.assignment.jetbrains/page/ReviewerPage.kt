package com.github.ferusm.assignment.jetbrains.page

import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Text

const val REVIEWER_PAGE_URL = "/reviewer"

@Composable
fun ReviewerPage() {

//    scope.launch {
//        runCatching { ReviewerDataResource.read() }
//            .recover { it.message }
//            .map { data.value = it ?: "" }
//    }

    Text("Reviewer page")
//    Text("Reviewer data: ${data.value}")
    A(HOME_PAGE_URL) {
        Text("На главную")
    }
}