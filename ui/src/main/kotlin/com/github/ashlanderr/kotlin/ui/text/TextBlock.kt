package com.github.ashlanderr.kotlin.ui.text

import com.github.ashlanderr.kotlin.ui.core.AbstractNode
import com.github.ashlanderr.kotlin.ui.core.Constraint
import com.github.ashlanderr.kotlin.ui.core.withTransform
import com.github.ashlanderr.kotlin.ui.layout.HorizontalAlign
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.geom.AffineTransform

class TextBlock(
    var text: String,
    var wrapping: TextWrapping = TextWrapping.NO_WRAP,
    var align: HorizontalAlign = HorizontalAlign.LEFT,
    var color: Color? = null,
    var fontFamily: String? = null,
    var fontStyle: Int? = null,
    var fontSize: Double? = null,
    key: Any? = null
) : AbstractNode(key) {

    private data class Line(var left: Double, var width: Double, val text: String)

    private var lines: List<Line> = emptyList()

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        applyStyle(g)
        val fm = g.fontMetrics
        val breaks = wrapping.split(text, fm, w.size.toInt())

        lines = breaks.windowed(2, 1).map { (a, b) ->
            val lineText = text.substring(a, b)
            val lineWidth = fm.stringWidth(lineText).toDouble()
            Line(0.0, lineWidth, lineText)
        }

        val linesWidth = lines.map { it.width }.max() ?: 0.0
        renderWidth = w.compute(linesWidth, w.size)
        renderHeight = lines.size * g.fontMetrics.height.toDouble()

        lines.forEach { it.left = align.computeLeft(it.width, renderWidth) }
    }

    override fun arrange(transform: AffineTransform) {
        renderTransform = transform
    }

    override fun render(g: Graphics2D) = g.withTransform(renderTransform) {
        applyStyle(g)
        val fm = g.fontMetrics
        var top = fm.ascent

        for (line in lines) {
            g.drawString(line.text, line.left.toInt(), top)
            top += fm.height
        }
    }

    private fun applyStyle(g: Graphics2D) {
        val defaultStyle = DefaultTextStyle.of(this)
        g.font = Font(
            fontFamily ?: defaultStyle.fontFamily,
            fontStyle ?: defaultStyle.fontStyle,
            (fontSize ?: defaultStyle.fontSize).toInt())
        g.color = color ?: defaultStyle.color
    }
}