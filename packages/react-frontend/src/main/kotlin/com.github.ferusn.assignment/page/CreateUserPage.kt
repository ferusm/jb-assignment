package com.github.ferusn.assignment.page


import com.github.ferusm.assignment.jetbrains.model.User
import com.github.ferusm.assignment.jetbrains.role.Role
import com.github.ferusn.assignment.provider.HttpClientProvider
import com.github.ferusn.assignment.resource.UserResource
import com.github.ferusn.assignment.util.UnauthorizedRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onInputFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import react.Props
import react.dom.attrs
import react.dom.option
import react.fc
import react.router.useNavigate
import react.useState
import styled.*


val CreateUser = fc<Props> {
    var name by useState("")
    var identifier by useState("")
    var role by useState<Role?>(null)
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
                onSubmitFunction = {
                    it.preventDefault()
                    CoroutineScope(Dispatchers.Main).launch {
                        if (role == null) {
                            return@launch
                        }
                        val user = User(name, identifier, role!!)
                        UserResource.create(user, HttpClientProvider.regular)
                        navigation("/login")

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
                    +"??????????"
                }
                styledInput(InputType.text) {
                    attrs {
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
                    +"????????????"
                }
                styledInput(InputType.password) {
                    attrs {
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
                    justifyContent = JustifyContent.spaceBetween
                }
                styledSpan {
                    +"????????"
                }
                styledSelect {
                    attrs {
                        onChangeFunction = {
                            val event = it.target as HTMLSelectElement
                            println(event.value)
                            role = Role.get(event.value)
                        }
                    }
                    css {
                        outline = Outline.none
                        border = "none"
                        backgroundColor = Color.transparent
                        borderBottom = "0.1rem solid grey"

                    }
                    option {
                        attrs {
                            selected = true
                        }
                        +""
                    }
                    Role.get().filter { it != UnauthorizedRole }.map { it.name }.map {
                        option {
                            attrs {
                                selected = it == role?.name
                            }
                            key = it
                            +it
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
                        value = "??????????????"
                    }
                    css {
                        display = Display.block
                        border = "0.1rem solid grey"
                        padding = "0.4rem"
                        backgroundColor = Color.transparent
                    }
                }
                styledButton {
                    +"??????????"
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