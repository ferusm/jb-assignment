package com.github.ferusn.assignment


import csstype.important
import kotlinx.browser.document
import kotlinx.css.*
import react.dom.render
import react.router.dom.BrowserRouter
import styled.css
import styled.styledDiv

fun main() {
    val rootDiv = document.getElementById("root")!!
    render(rootDiv) {
        styledDiv {
            css {
                overflowWrap = OverflowWrap.normal
                maxWidth = 80.vh
                child("*") {
                    fontFamily = important("'Roboto', sans-serif;")
                    fontSize = important(12.pt)
                }
            }
            BrowserRouter { Application() }
        }
    }
}