package com.github.ashlanderr.kotlin.ui.text

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Color
import java.awt.Font

data class TextStyle(
    val color: Color = Color.BLACK,
    val fontFamily: String = Font.SANS_SERIF,
    val fontStyle: Int = Font.PLAIN,
    val fontSize: Int = 12
)

class DefaultTextStyle(
    @ReactiveProperty var data: TextStyle,
    @ReactiveProperty var child: Node
) : StatelessComponent() {
    companion object {
        private val DEFAULT_STYLE = TextStyle()
        fun of(node: Node) = node.ancestor<DefaultTextStyle>()?.data ?: DEFAULT_STYLE
        fun of(state: State) = of(state.component)
    }

    override fun render() = child
}