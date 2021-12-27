package com.github.ferusn.assignment.page

import com.github.ferusm.assignment.jetbrains.model.User
import com.github.ferusm.assignment.jetbrains.role.AdminRole
import com.github.ferusm.assignment.jetbrains.role.ReviewerRole
import com.github.ferusm.assignment.jetbrains.role.UserRole
import com.github.ferusn.assignment.resource.AdminAreaResource
import com.github.ferusn.assignment.resource.ReviewerAreaResource
import com.github.ferusn.assignment.resource.UserAreaResource
import com.github.ferusn.assignment.resource.UserResource
import com.github.ferusn.assignment.util.UnauthorizedRole
import io.ktor.client.*
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.Props
import react.dom.attrs
import react.dom.span
import react.fc
import react.router.useNavigate
import react.useEffect
import react.useState
import styled.css
import styled.styledA
import styled.styledButton
import styled.styledDiv

external interface HomeProps : Props {
    var client: HttpClient?
    var onLogout: () -> Unit
}

val Home = fc<HomeProps> { props ->
    var user by useState<User?>(null)
    val navigate = useNavigate()


    useEffect(props.client) {
        val client = props.client
        if (client == null) {
            navigate("/login")
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                user = UserResource.read(client)
            }
        }
    }

    styledDiv {
        css {
            display = Display.flex
            alignItems = Align.center
            flexDirection = FlexDirection.column
            justifyContent = JustifyContent.center
            width = 100.vw
            minHeight = 100.vh
            rowGap = 2.rem
        }
        styledDiv {
            css {
                display = Display.flex
                flexDirection = FlexDirection.column
                alignItems = Align.center
                justifyContent = JustifyContent.center
                rowGap = 1.rem
                width = LinearDimension.minContent
            }
            styledDiv {
                css {
                    width = 100.pct
                    columnGap = 2.rem
                    display = Display.flex
                    justifyContent = JustifyContent.spaceAround
                }
                span {
                    +"Логин: ${user?.name ?: ""}"
                }
            }
            styledDiv {
                css {
                    width = 100.pct
                    columnGap = 2.rem
                    display = Display.flex
                    justifyContent = JustifyContent.spaceAround
                }
                span {
                    +"Роль: ${user?.role?.name ?: ""}"
                }
            }
            styledDiv {
                css {
                    width = 100.pct
                    columnGap = 2.rem
                    display = Display.flex
                    justifyContent = JustifyContent.spaceAround
                }
                styledA {
                    attrs {
                        onClickFunction = {
                            it.preventDefault()
                            CoroutineScope(Dispatchers.Main).launch {
                                val response = AdminAreaResource.hello(props.client!!)
                                window.alert(response)
                            }
                        }
                    }
                    css {
                        hover {
                            cursor = Cursor.pointer
                        }
                        val role = user?.role ?: UnauthorizedRole
                        color = if (role.isHaveRights(AdminRole)) {
                            Color.lightBlue
                        } else {
                            Color.orangeRed
                        }
                    }
                    +"Admin"
                }
                styledA {
                    attrs {
                        onClickFunction = {
                            it.preventDefault()
                            CoroutineScope(Dispatchers.Main).launch {
                                val response = ReviewerAreaResource.hello(props.client!!)
                                window.alert(response)
                            }
                        }
                    }
                    css {
                        hover {
                            cursor = Cursor.pointer
                        }
                        val role = user?.role ?: UnauthorizedRole
                        color = if (role.isHaveRights(ReviewerRole)) {
                            Color.lightBlue
                        } else {
                            Color.orangeRed
                        }
                    }
                    +"Reviewer"
                }
                styledA {
                    attrs {
                        onClickFunction = {
                            it.preventDefault()
                            CoroutineScope(Dispatchers.Main).launch {
                                val response = UserAreaResource.hello(props.client!!)
                                window.alert(response)
                            }
                        }
                    }
                    css {
                        hover {
                            cursor = Cursor.pointer
                        }
                        val role = user?.role ?: UnauthorizedRole
                        color = if (role.isHaveRights(UserRole)) {
                            Color.lightBlue
                        } else {
                            Color.orangeRed
                        }
                    }
                    +"User"
                }
            }
            styledDiv {
                css {
                    width = 100.pct
                    columnGap = 2.rem
                    display = Display.flex
                    justifyContent = JustifyContent.spaceAround
                }
                styledButton {
                    +"Сменить пароль"
                    css {
                        display = Display.block
                        border = "0.1rem solid grey"
                        padding = "0.4rem"
                        backgroundColor = Color.transparent
                    }
                    attrs {
                        onClickFunction = {
                            it.preventDefault()
                            navigate("/change-password")
                        }
                    }
                }
                styledButton {
                    +"Выйти"
                    css {
                        display = Display.block
                        border = "0.1rem solid grey"
                        padding = "0.4rem"
                        backgroundColor = Color.transparent
                    }
                    attrs {
                        onClickFunction = {
                            it.preventDefault()
                            props.onLogout()
                        }
                    }
                }
            }
        }
    }
}