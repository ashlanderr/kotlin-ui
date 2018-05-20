package com.github.ashlanderr.kotlin.ui.material

import com.github.ashlanderr.kotlin.ui.core.*

class FlatButton : StatefulComponent<FlatButtonState, FlatButton>() {
    @ReactiveProperty
    var content: Node = EmptyNode

    @ReactiveProperty
    var onClick: () -> Unit = {}

    override fun initState(component: FlatButton) = FlatButtonState(component)
}

class FlatButtonState(override val component: FlatButton) : State() {

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