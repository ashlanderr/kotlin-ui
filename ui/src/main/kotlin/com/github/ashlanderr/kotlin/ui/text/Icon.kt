package com.github.ashlanderr.kotlin.ui.text

import com.github.ashlanderr.kotlin.ui.core.AbstractNode
import com.github.ashlanderr.kotlin.ui.core.Constraint
import com.github.ashlanderr.kotlin.ui.core.Node
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D

class Icon(
    var icon: IconData,
    var size: Double? = null,
    var color: Color? = null,
    key: Any? = null
) : AbstractNode(key) {
    private val chars = CharArray(1)
    private var offset = 0.0

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        applyStyle(g)
        val fm = g.fontMetrics
        val iconWidth = fm.charWidth(icon.codePoint.toChar()).toDouble()
        val iconHeight = fm.height.toDouble()
        offset = (iconHeight - iconWidth) / 2
        renderWidth = iconHeight
        renderHeight = iconHeight
    }

    override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top
    }

    override fun render(g: Graphics2D) {
        applyStyle(g)
        val fm = g.fontMetrics
        chars[0] = icon.codePoint.toChar()
        g.drawChars(chars, 0, 1, (renderLeft + offset).toInt(), (renderTop + fm.ascent).toInt())
    }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }

    private fun applyStyle(g: Graphics2D) {
        val defaultStyle = DefaultTextStyle.of(this)
        g.font = Font(icon.fontFamily, Font.PLAIN, size?.toInt() ?: defaultStyle.fontSize)
        g.color = color ?: defaultStyle.color
    }
}

data class IconData(
    val codePoint: Int,
    val fontFamily: String
)