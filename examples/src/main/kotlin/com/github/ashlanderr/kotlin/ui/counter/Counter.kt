package com.github.ashlanderr.kotlin.ui.counter

import com.github.ashlanderr.kotlin.ui.core.Application
import com.github.ashlanderr.kotlin.ui.core.Component
import com.github.ashlanderr.kotlin.ui.core.State
import com.github.ashlanderr.kotlin.ui.layout.*
import com.github.ashlanderr.kotlin.ui.material.flatButton
import com.github.ashlanderr.kotlin.ui.text.textBlock
import javax.swing.JFrame

class Counter : Component<CounterState, Counter>() {
    override fun initState() = CounterState()
}

class CounterState : State<CounterState, Counter>() {
    private var count = 0

    override fun render() = column {
        align = HorizontalAlign.STRETCH
        +padding {
            child = textBlock {
                text = "Button pressed $count times"
            }
        }
        +padding {
            padding = Indent.symetric(0.0, 8.0)
            child = flatButton {
                onClick = {
                    update {
                        count += 1
                    }
                }
                content = padding {
                    padding = Indent.symetric(8.0, 8.0)
                    child = textBlock {
                        align = HorizontalAlign.CENTER
                        text = "PUSH ME"
                    }
                }
            }
        }
    }
}

class CounterApp : Application() {
    override fun render() = center {
        child = Counter()
    }
}

fun main(args: Array<String>) {
    val app = CounterApp()
    app.setBounds(200, 200, 800, 600)
    app.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    app.isVisible = true
}