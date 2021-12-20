package com.github.ferusn.assignment

import com.github.ferusm.assignment.jetbrains.model.TokenPair
import com.github.ferusn.assignment.page.*
import com.github.ferusn.assignment.provider.HttpClientProvider
import io.ktor.client.*
import kotlinx.browser.localStorage
import org.w3c.dom.get
import org.w3c.dom.set
import react.*
import react.router.Route
import react.router.Routes
import react.router.useNavigate

val Application = fc<Props> {
    val accessToken = localStorage["access"]
    val refreshToken = localStorage["refresh"]
    val initClient = if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
        val tokens = TokenPair(accessToken, refreshToken)
        HttpClientProvider.authenticated(tokens) { refreshedTokens ->
            localStorage["access"] = refreshedTokens.access
            localStorage["refresh"] = refreshedTokens.refresh
        }
    } else {
        null
    }

    var authenticatedClient by useState(initClient)
    val navigate = useNavigate()

    Routes {
        Route {
            attrs {
                path = "/"
                element = createElement {
                    child(Home) {
                        attrs.client = authenticatedClient
                        attrs.onLogout = {
                            authenticatedClient = null
                            localStorage.removeItem("access")
                            localStorage.removeItem("refresh")
                            navigate("/login")
                        }
                    }
                }
            }
        }
        Route {
            attrs {
                path = "/login"
                element = createElement {
                    child(Login) {
                        attrs.onLogin = { tokens ->
                            localStorage["access"] = tokens.access
                            localStorage["refresh"] = tokens.refresh
                            authenticatedClient = HttpClientProvider.authenticated(tokens) { refreshedTokens ->
                                localStorage["access"] = refreshedTokens.access
                                localStorage["refresh"] = refreshedTokens.refresh
                            }
                        }
                    }
                }
            }
        }
        Route {
            attrs {
                path = "/create-user"
                element = createElement { CreateUser() }
            }
        }
        Route {
            attrs {
                path = "/change-password"
                    element = createElement {
                        child(ChangePassword) {
                            attrs.client = authenticatedClient
                        }
                    }
            }
        }
    }
}