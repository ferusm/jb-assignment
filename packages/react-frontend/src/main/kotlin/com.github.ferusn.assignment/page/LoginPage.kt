package com.github.ferusn.assignment.page

import com.github.ferusm.assignment.jetbrains.model.Credentials
import com.github.ferusm.assignment.jetbrains.model.TokenPair
import com.github.ferusn.assignment.resource.AuthResource
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

external interface LoginProps : Props {
    var onLogin: (pair: TokenPair) -> Unit
}

val Login = fc<LoginProps> { props ->
    var name by useState("")
    var identifier by useState("")
    val navigation = useNavigate()

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
            attrs {
                onSubmitFunction = { event ->
                    event.preventDefault()
                    CoroutineScope(Dispatchers.Main).launch {
                        val credentials = Credentials(name, identifier)
                        val tokenPair = AuthResource.create(credentials)
                        props.onLogin(tokenPair)
                        navigation("/")
                    }
                }
            }
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
                    justifyContent = JustifyContent.spaceBetween
                }
                styledSpan {
                    +"Логин"
                }
                styledInput(InputType.text) {
                    attrs {
                        value = name
                        onInputFunction = {
                            val event = it.target as HTMLInputElement
                            name = event.value
                        }
                    }
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
                    attrs {
                        value = identifier
                        onInputFunction = {
                            val event = it.target as HTMLInputElement
                            identifier = event.value
                        }
                    }
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
                        value = "Войти"
                    }
                    css {
                        display = Display.block
                        border = "0.1rem solid grey"
                        padding = "0.4rem"
                        backgroundColor = Color.transparent
                    }
                }
                styledButton {
                    +"Создать"
                    attrs {
                        onClickFunction = {
                            it.preventDefault()
                            navigation("/create-user")
                        }
                    }
                    css {
                        display = Display.block
                        border = "0.1rem solid grey"
                        padding = "0.4rem"
                        backgroundColor = Color.transparent
                    }
                }
            }
        }
    }
}