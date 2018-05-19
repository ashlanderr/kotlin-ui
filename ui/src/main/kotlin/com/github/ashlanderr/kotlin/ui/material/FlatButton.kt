package com.github.ashlanderr.kotlin.ui.material

import com.github.ashlanderr.kotlin.ui.core.*

class FlatButton : Component<FlatButtonState, FlatButton>() {
    @ReactiveProperty
    var content: Node = EmptyNode

    @ReactiveProperty
    var onClick: () -> Unit = {}

    override fun initState() = FlatButtonState()
}

class FlatButtonState : State<FlatButtonState, FlatButton>() {

    override fun render(): Node {
        return eventListener {
            onMouseClick = {
                component.onClick()
                false
            }
            child = rippleEffect {
                child = component.content
            }
        }
    }
}

fun flatButton(builder: Builder<FlatButton>) = build(builder)