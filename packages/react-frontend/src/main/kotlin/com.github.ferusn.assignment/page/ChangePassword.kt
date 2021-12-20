package com.github.ferusn.assignment.page


import com.github.ferusm.assignment.jetbrains.model.Password
import com.github.ferusn.assignment.resource.UserResource
import io.ktor.client.*
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onInputFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import react.Props
import react.dom.attrs
import react.fc
import react.router.useNavigate
import react.useState
import styled.*

external interface ChangePasswordProps: Props {
    var client: HttpClient?
}

val ChangePassword = fc<ChangePasswordProps> { props ->
    val navigate = useNavigate()
    var identifer by useState("")

    if (props.client == null) {
        navigate("/login")
    }
    styledDiv {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = Align.center
            justifyContent = JustifyContent.center
            width = 100.vw
            minHeight = 100.vh
        }
        styledForm {
            css {
                display = Display.flex
                flexDirection = FlexDirection.column
                alignItems = Align.center
                justifyContent = JustifyContent.center
                rowGap = 1.rem
                width = LinearDimension.minContent
            }
            attrs {
                onSubmitFunction = {
                    it.preventDefault()
                    CoroutineScope(Dispatchers.Main).launch {
                        val password = Password(identifer)
                        runCatching {
                            UserResource.update(password, props.client!!)
                        }.onSuccess {
                            navigate("/")
                        }.onFailure { exception ->
                            window.alert(exception.message ?: "error")
                        }
                    }
                }
            }
            styledDiv {
                css {
                    width = 100.pct
                    columnGap = 2.rem
                    display = Display.flex
                    justifyContent = JustifyContent.spaceBetween
                }
                styledSpan {
                    +"Пароль"
                }
                styledInput(InputType.password) {
                    css {
                        outline = Outline.none
                        border = "none"
                        backgroundColor = Color.transparent
                        borderBottom = "0.1rem solid grey"
                        focus {
                            border = "none"
                            borderBottom = "0.1rem solid black"
                        }
                    }
                    attrs {
                        value = identifer
                        onInputFunction = {
                            val target = it.target as HTMLInputElement
                            identifer = target.value
                        }
                    }
                }
            }
            styledDiv {
                css {
                    width = 100.pct
                    columnGap = 2.rem
                    display = Display.flex
                    justifyContent = JustifyContent.spaceAround
                }
                styledInput(InputType.submit) {
                    attrs {
                        value = "Сменить"
                    }
                    css {
                        display = Display.block
                        border = "0.1rem solid grey"
                        padding = "0.4rem"
                        backgroundColor = Color.transparent
                    }
                }
                styledButton {
                    +"Назад"
                    css {
                        display = Display.block
                        border = "0.1rem solid grey"
                        padding = "0.4rem"
                        backgroundColor = Color.transparent
                    }
                    attrs {
                        onClickFunction = {
                            navigate("/")
                        }
                    }
                }
            }
        }
    }
}