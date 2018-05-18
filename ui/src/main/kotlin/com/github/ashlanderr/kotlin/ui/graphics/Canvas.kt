package com.github.ashlanderr.kotlin.ui.graphics

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics

typealias CanvasHandler = (g: Graphics) -> Unit

class Canvas : AbstractNode() {
    @ReactiveProperty
    var render: CanvasHandler? = null

    override fun measure(g: Graphics, w: Constraint, h: Constraint) {
        renderWidth = w.compute(0.0, w.size)
        renderHeight = h.compute(0.0, h.size)
    }

    override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top
    }

    override fun render(g: Graphics) {
        val clip = g.pushClip(renderLeft.toInt(), renderTop.toInt(), renderWidth.toInt(), renderHeight.toInt())

        g.translate(renderLeft.toInt(), renderTop.toInt())
        render?.invoke(g)
        g.translate(-renderLeft.toInt(), -renderTop.toInt())

        g.popClip(clip)
    }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }
}

fun canvas(render: CanvasHandler) = Canvas().apply {
    this.render = render
}