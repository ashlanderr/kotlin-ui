package com.github.ashlanderr.text

import com.github.ashlanderr.core.*
import java.awt.Color
import java.awt.Graphics

class TextBlock : AbstractNode() {
    private var lines: List<String> = emptyList()

    @ReactiveProperty
    var text: String = ""

    @ReactiveProperty
    var wrapping: TextWrapping = TextWrapping.BREAK_WORD

    override fun measure(g: Graphics, maxWidth: Double, maxHeight: Double) {
        val fm = g.fontMetrics
        val breaks = wrapping.split(text, fm, maxWidth.toInt())
        lines = breaks.windowed(2, 1).map { (a, b) -> text.substring(a, b) }

        renderWidth = maxWidth
        renderHeight = lines.size * g.fontMetrics.height.toDouble() + fm.descent
    }

    override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top
    }

    override fun render(g: Graphics) {
        val fm = g.fontMetrics
        var top = renderTop + fm.height

        g.color = Color.BLACK

        for (line in lines) {
            g.drawString(line, renderLeft.toInt(), top.toInt())
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