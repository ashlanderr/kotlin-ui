package com.github.ashlanderr.kotlin.ui.text

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Color

class TextTheme : StatefulComponent<TextThemeState, TextTheme>() {
    companion object {
        val DEFAULT = TextTheme()

        fun of(state: State): TextTheme = state.ancestorNode() ?: DEFAULT
        fun of(node: Node): TextTheme = node.ancestor() ?: DEFAULT
    }

    @ReactiveProperty
    var child: Node = EmptyNode

    @ReactiveProperty
    var color: Color = Color.BLACK

    override fun initState(component: TextTheme) = TextThemeState(component)
}

class TextThemeState(override val component: TextTheme) : State() {
    override fun render() = component.child
}

fun textTheme(builder: Builder<TextTheme>) = build(builder)