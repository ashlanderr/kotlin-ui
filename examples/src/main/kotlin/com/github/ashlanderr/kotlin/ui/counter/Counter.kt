package com.github.ashlanderr.kotlin.ui.counter

import com.github.ashlanderr.kotlin.ui.core.Application
import com.github.ashlanderr.kotlin.ui.core.State
import com.github.ashlanderr.kotlin.ui.core.StatefulComponent
import com.github.ashlanderr.kotlin.ui.layout.*
import com.github.ashlanderr.kotlin.ui.material.ButtonStyle
import com.github.ashlanderr.kotlin.ui.material.FlatButton
import com.github.ashlanderr.kotlin.ui.text.TextBlock
import javax.swing.JFrame

class Counter : StatefulComponent<CounterState, Counter>() {
    override fun initState(component: Counter) = CounterState(component)
}

class CounterState(override val component: Counter) : State() {
    private var count = 0

    override fun render() = Column(
        align = HorizontalAlign.STRETCH,
        children = mutableListOf(
            TextBlock(
                text = "Button pressed $count times"
            ),
            Padding(
                padding = Indent.symmetric(0.0, 8.0),
                child = FlatButton(
                    style = ButtonStyle.ACCENT,
                    onClick = {
                        update {
                            count += 1
                        }
                    },
                    content = Padding(
                        padding = Indent.symmetric(8.0, 8.0),
                        child = TextBlock(
                            align = HorizontalAlign.CENTER,
                            text = "PUSH ME"
                        )
                    )
                )
            )
        )
    )
}

class CounterApp : Application() {
    override fun render() = Center(Counter())
}

fun main(args: Array<String>) {
    val app = CounterApp()
    app.setBounds(200, 200, 800, 600)
    app.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    app.isVisible = true
}