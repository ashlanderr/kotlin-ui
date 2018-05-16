package com.github.ashlanderr.kotlin.ui.helloworld

import com.github.ashlanderr.kotlin.ui.core.Application
import com.github.ashlanderr.kotlin.ui.layout.center
import com.github.ashlanderr.kotlin.ui.text.textBlock

class HelloWorldApp : Application() {
    override fun render() = center {
        child = textBlock {
            text = "Hello, World!"
        }
    }
}

fun main(args: Array<String>) {
    val app = HelloWorldApp()
    //app.isUndecorated = true
    app.setBounds(200, 200, 800, 600)
    app.isVisible = true
}