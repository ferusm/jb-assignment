package com.github.ferusm.assignment.jetbrains.page

import androidx.compose.runtime.*
import com.github.ferusm.assignment.jetbrains.model.User
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Text

const val USER_PAGE_URL = "/user"

@Composable
fun UserPage(state: MutableState<User?>) {
    val scope = rememberCoroutineScope()
    val data = remember { mutableStateOf("") }

//    scope.launch {
//        runCatching { UserDataResource.read() }
//            .recover { it.message }
//            .map { data.value = it ?: "" }
//    }

    Text("User page")
    Text("User data: ${data.value}")
    A(HOME_PAGE_URL) {
        Text("На главную")
    }
}