package com.github.ferusm.assignment.jetbrains

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import app.softwork.routingcompose.BrowserRouter
import com.github.ferusm.assignment.jetbrains.model.User
import com.github.ferusm.assignment.jetbrains.page.*

@Composable
fun Application() {
    val state = remember { mutableStateOf<User?>(null) }

    BrowserRouter(HOME_PAGE_URL) {
        route(LOGIN_PAGE_URL) { SessionsCreatePage() }
        route(USERS_CREATE_PAGE_URL) { UsersCreatePage() }
        route(HOME_PAGE_URL) { HomePage(state) }
    }

//    initRouting(HOME_PAGE_URL)
//
//    Router {
//        route(LOGIN_PAGE_URL, exact = true) { SessionsCreatePage() }
//        route(USERS_CREATE_PAGE_URL, exact = true) { UsersCreatePage() }
//    }

//    HashRouter("/") {
//        route("/") {
//            HomePage(state)
//        }
//        route("admin") { AdminPage() }
//        route("user") { UserPage(state) }
//        route("reviewer") { ReviewerPage() }
//        route("users") {
//            route("create") { UsersCreatePage() }
//            route("password") { route("change") { PasswordChangePage(state) } }
//        }
//        route("sessions") {
//            route("create") { SessionsCreatePage() }
//        }
//    }
}