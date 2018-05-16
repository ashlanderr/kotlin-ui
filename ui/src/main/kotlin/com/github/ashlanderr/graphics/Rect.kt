package com.github.ashlanderr.graphics

import com.github.ashlanderr.core.*
import java.awt.Color
import java.awt.Graphics

class Rect : AbstractNode() {
    @ReactiveProperty
    var width: Double? = null

    @ReactiveProperty
    var height: Double? = null

    @ReactiveProperty
    var fill: Color? = Color.WHITE

    @ReactiveProperty
    var stroke: Color? = Color.BLACK

    override fun measure(g: Graphics, maxWidth: Double, maxHeight: Double) {
        renderWidth = width ?: maxWidth
        renderHeight = height ?: maxHeight
    }

    override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top
    }

    override fun render(g: Graphics) {
        if (fill != null) {
            g.color = fill
            g.fillRect(renderLeft.toInt(), renderTop.toInt(), renderWidth.toInt(), renderHeight.toInt())
        }
        if (stroke != null) {
            g.color = stroke
            g.drawRect(renderLeft.toInt(), renderTop.toInt(), renderWidth.toInt() - 1, renderHeight.toInt() - 1)
        }
    }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }
}

fun rect(builder: Builder<Rect>) = build(builder)