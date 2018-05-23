package com.github.ashlanderr.kotlin.ui.helloworld

import com.github.ashlanderr.kotlin.ui.core.Application
import com.github.ashlanderr.kotlin.ui.layout.Center
import com.github.ashlanderr.kotlin.ui.material.Window
import com.github.ashlanderr.kotlin.ui.text.TextBlock
import javax.swing.JFrame

class HelloWorldApp : Application() {
    override fun render() = Window(
        title = TextBlock(
            text = "Hello, World!"
        ),
        content = Center(
            child = TextBlock(
                text = "Hello, World!"
            )
        )
    )
}

fun main(args: Array<String>) {
    val app = HelloWorldApp()
    app.isUndecorated = true
    app.setBounds(200, 200, 800, 600)
    app.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    app.isVisible = true
}