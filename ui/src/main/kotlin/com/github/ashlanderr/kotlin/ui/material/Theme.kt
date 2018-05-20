package com.github.ashlanderr.kotlin.ui.material

import com.github.ashlanderr.kotlin.ui.core.Node
import com.github.ashlanderr.kotlin.ui.core.ReactiveProperty
import com.github.ashlanderr.kotlin.ui.core.StatelessComponent
import com.github.ashlanderr.kotlin.ui.core.ancestor
import com.github.ashlanderr.kotlin.ui.graphics.background
import com.github.ashlanderr.kotlin.ui.text.textTheme
import java.awt.Color

data class ThemeData(
    val primary1Color: Color = Color(0, 188, 212),
    val primary2Color: Color = Color(0, 151, 167),
    val primary3Color: Color = Color(189, 189, 189),
    val accent1Color: Color = Color(255, 64, 129),
    val accent2Color: Color = Color(245, 245, 245),
    val accent3Color: Color = Color(158, 158, 158),
    val textColor: Color = Color(0, 0, 0, 221),
    val secondaryTextColor: Color = Color(0, 0, 0, 138),
    val alternateTextColor: Color = Color(255, 255, 255),
    val canvasColor: Color = Color(255, 255, 255),
    val borderColor: Color = Color(224, 224, 224),
    val disabledColor: Color = Color(0, 0, 0, 77),
    val shadowColor: Color = Color(0, 0, 0)
)

class Theme(
    @ReactiveProperty var data: ThemeData,
    @ReactiveProperty var child: Node
) : StatelessComponent() {
    companion object {
        private val DEFAULT_THEME = ThemeData()
        fun of(node: Node) = node.ancestor<Theme>()?.data ?: DEFAULT_THEME
    }

    override fun render() = background {
        color = data.canvasColor
        child = textTheme {
            color = data.textColor
            child = this@Theme.child
        }
    }
}