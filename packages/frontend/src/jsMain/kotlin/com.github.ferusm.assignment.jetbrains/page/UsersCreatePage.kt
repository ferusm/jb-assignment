package com.github.ferusm.assignment.jetbrains.page

import androidx.compose.runtime.*
import com.github.ferusm.assignment.jetbrains.model.Role
import com.github.ferusm.assignment.jetbrains.model.User
import com.github.ferusm.assignment.jetbrains.resource.UserResource
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.onSubmit
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.selected
import org.jetbrains.compose.web.dom.*


const val USERS_CREATE_PAGE_URL = "/create-user"

@Composable
fun UsersCreatePage() {
    val scope = rememberCoroutineScope()
    val name = remember { mutableStateOf("") }
    val identifier = remember { mutableStateOf("") }
    val role = remember { mutableStateOf(Role.ADMIN) }

    Form("#", {
        onSubmit {
            it.preventDefault()
            scope.launch {
                val username by name
                val password by identifier
                val userRole by role
                val user = User(username, password, userRole)
                runCatching {
                    UserResource.create(user)
                }.onSuccess {

                }
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
        Select({
            onChange {
                role.value = Role.valueOf(it.value ?: Role.UNKNOWN.name)
            }
        }, false, {
            Option("${Role.ADMIN}", {
                selected()
            }) {
                Text("${Role.ADMIN}")
            }
            Option("${Role.REVIEWER}") {
                Text("${Role.REVIEWER}")
            }
            Option("${Role.USER}") {
                Text("${Role.USER}")
            }
        })
        SubmitInput {
            value("Создать")
        }
    })

    A(HOME_PAGE_URL) {
        Text("На главную")
    }
}