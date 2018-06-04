package com.github.ashlanderr.kotlin.ui.text

import com.github.ashlanderr.kotlin.ui.core.AbstractNode
import com.github.ashlanderr.kotlin.ui.core.Constraint
import com.github.ashlanderr.kotlin.ui.core.withTransform
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.geom.AffineTransform

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

    override fun arrange(transform: AffineTransform) {
        renderTransform = transform
    }

    override fun render(g: Graphics2D) = g.withTransform(renderTransform) {
        applyStyle(g)
        val fm = g.fontMetrics
        chars[0] = icon.codePoint.toChar()
        g.drawChars(chars, 0, 1, offset.toInt(), fm.ascent)
    }

    private fun applyStyle(g: Graphics2D) {
        val defaultStyle = DefaultTextStyle.of(this)
        g.font = Font(icon.fontFamily, Font.PLAIN, (size ?: defaultStyle.fontSize).toInt())
        g.color = color ?: defaultStyle.color
    }
}

data class IconData(
    val codePoint: Int,
    val fontFamily: String
)