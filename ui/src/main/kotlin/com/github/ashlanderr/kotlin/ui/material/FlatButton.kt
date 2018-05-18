package com.github.ashlanderr.kotlin.ui.material

import com.github.ashlanderr.kotlin.ui.core.*
import com.github.ashlanderr.kotlin.ui.layout.stack

class FlatButton : Component<FlatButtonState, FlatButton>() {
    var content: Node = EmptyNode
    var onClick: () -> Unit = {}

    override fun initState() = FlatButtonState()
}

class FlatButtonState : State<FlatButtonState, FlatButton>() {
    private val ripples = mutableListOf<RippleEffect>()

    override fun render() = eventListener {
        onMouseClick = {
            component.onClick()
            addRipple(it.point)
            false
        }
        child = stack {
            +component.content
            ripples.forEach { +it }
        }
    }

    private fun addRipple(point: Point) {
        update {
            val ripple = rippleEffect {
                x = point.x
                y = point.y
            }
            ripple.key = ripple
            ripple.onCompleted = { ripples.remove(ripple) }
            ripples.add(ripple)
        }
    }
}

fun flatButton(builder: Builder<FlatButton>) = build(builder)