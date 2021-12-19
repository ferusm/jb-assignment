package com.github.ferusm.assignment.jetbrains.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import com.github.ferusm.assignment.jetbrains.delegate.TokensDelegate
import com.github.ferusm.assignment.jetbrains.model.Role
import com.github.ferusm.assignment.jetbrains.model.User
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Text

const val HOME_PAGE_URL = ""

@Composable
fun HomePage(state: MutableState<User?>) {
    val tokens by TokensDelegate
    val user by state
    if(tokens == null || user == null) {
//        Router.current.navigate(LOGIN_PAGE_URL)
        return
    }

    Text("Current user: ${user?.name ?: "unknown"}")
    Text("Current user role: ${user?.role ?: "unknown"}")
    Text("Current Access token: ${tokens?.access ?: "unknown"}")
    Text("Current Refresh token: ${tokens?.refresh ?: "unknown"}")


    if (user?.role?.check(Role.ADMIN) == true) {
        A(ADMIN_PAGE_URL) {
            Text("AMIN ресурс")
        }
    }
    if (user?.role?.check(Role.REVIEWER) == true) {
        A(REVIEWER_PAGE_URL) {
            Text("REVIEWER ресурс")
        }
    }
    if (user?.role?.check(Role.USER) == true) {
        A(USER_PAGE_URL) {
            Text("USER ресурс")
        }
    }

    A(PASSWORD_CHANGE_PAGE_URL) {
        Text("Сменить пароль")
    }


    A(PASSWORD_CHANGE_PAGE_URL, {
        onClick {
            state.value = null
        }
    }) {
        Text("Выйти")
    }
}