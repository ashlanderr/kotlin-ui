package com.github.ashlanderr.kotlin.ui.text

import com.github.ashlanderr.kotlin.ui.core.*
import com.github.ashlanderr.kotlin.ui.layout.HorizontalAlign
import java.awt.Color
import java.awt.Graphics

class TextBlock : AbstractNode() {
    private data class Line(var left: Double, var width: Double, val text: String)

    private var lines: List<Line> = emptyList()

    @ReactiveProperty
    var text: String = ""

    @ReactiveProperty
    var wrapping: TextWrapping = TextWrapping.BREAK_WORD

    @ReactiveProperty
    var align: HorizontalAlign = HorizontalAlign.LEFT

    override fun measure(g: Graphics, w: Constraint, h: Constraint) {
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

    override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top
    }

    override fun render(g: Graphics) {
        val fm = g.fontMetrics
        var top = renderTop + fm.ascent

        g.color = Color.BLACK

        for (line in lines) {
            g.drawString(line.text, (renderLeft + line.left).toInt(), top.toInt())
            top += fm.height
        }
    }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }
}

fun textBlock(builder: Builder<TextBlock>) = build(builder)