package com.github.ferusm.assignment.jetbrains.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.github.ferusm.assignment.jetbrains.model.User
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Text

const val PASSWORD_CHANGE_PAGE_URL = "/change-password"

@Composable
fun PasswordChangePage(state: MutableState<User?>) {
//    if (!state.value.isReady) {
//        Router.current.navigate(USERS_CREATE_PAGE_URL)
//        return
//    }
//    if (state.value.session.accessToken.isNullOrEmpty()) {
//        Router.current.navigate(LOGIN_PAGE_URL)
//        return
//    }
    Text("Change password page")
    A(HOME_PAGE_URL) {
        Text("На главную")
    }
}