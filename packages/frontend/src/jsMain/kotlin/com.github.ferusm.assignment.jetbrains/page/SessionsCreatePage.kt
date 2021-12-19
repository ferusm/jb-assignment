package com.github.ferusm.assignment.jetbrains.page

import androidx.compose.runtime.*
import com.github.ferusm.assignment.jetbrains.model.Credentials
import com.github.ferusm.assignment.jetbrains.model.Role
import com.github.ferusm.assignment.jetbrains.model.User
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.onSubmit
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.selected
import org.jetbrains.compose.web.dom.*

const val LOGIN_PAGE_URL = "/login"

@Composable
fun SessionsCreatePage() {
    val scope = rememberCoroutineScope()
    val name = remember { mutableStateOf("") }
    val identifier = remember { mutableStateOf("") }

    Form("#", {
        onSubmit {
            it.preventDefault()
            scope.launch {
                val username by name
                val password by identifier
                val credentials = Credentials(username, password)
                println(credentials)
            }
        }
    }, {
        TextInput(name.value) {
            placeholder("Имя пользователя")
            onInput {
                name.value = it.value
            }
        }
        PasswordInput(identifier.value) {
            placeholder("Пароль")
            onInput {
                identifier.value = it.value
            }
        }
        SubmitInput {
            value("Войти")
        }
    })

    A(USERS_CREATE_PAGE_URL) {
        Text("Создать пользователя")
    }
}