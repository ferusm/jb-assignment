package com.github.ferusm.assignment.jetbrains.page

import androidx.compose.runtime.*
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Text

const val ADMIN_PAGE_URL = "/admin"


@Composable
fun AdminPage() {
//    val scope = rememberCoroutineScope()
//    val data = remember { mutableStateOf("") }
//
//    if (!state.value.isReady) {
//        Router.current.navigate(USERS_CREATE_PAGE_URL)
//        return
//    }
//    if (state.value.session.accessToken.isNullOrEmpty()) {
//        Router.current.navigate(LOGIN_PAGE_URL)
//        return
//    }
//    if ((state.value.session.role?.ordinal ?: Int.MAX_VALUE) <= Role.ADMIN.ordinal) {
//        Router.current.navigate(HOME_PAGE_URL)
//        return
//    }
//    scope.launch {
//        runCatching { AdminDataResource.read() }
//            .recover { it.message }
//            .map { data.value = it ?: "" }
//    }

    Text("Admin page")
//    Text("Admin data: ${data.value}")
    A(HOME_PAGE_URL) {
        Text("На главную")
    }
}