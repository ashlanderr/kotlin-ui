package com.github.ashlanderr.kotlin.ui.graphics

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics2D

abstract class Canvas(key: Any? = null) : Node {
    final override var renderLeft: Double = 0.0
        private set

    final override var renderTop: Double = 0.0
        private set

    final override var renderWidth: Double = 0.0
        private set

    final override var renderHeight: Double = 0.0
        private set

    final override var parent: Node? = null
        private set

    final override var key: Any? = key
        private set

    final override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        renderWidth = w.compute(0.0, w.size)
        renderHeight = h.compute(0.0, h.size)
    }

    final override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top
    }

    final override fun render(g: Graphics2D) {
        val clip = g.pushClip(renderLeft.toInt(), renderTop.toInt(), renderWidth.toInt(), renderHeight.toInt())

        g.translate(renderLeft.toInt(), renderTop.toInt())
        render(g, renderWidth, renderHeight)
        g.translate(-renderLeft.toInt(), -renderTop.toInt())

        g.popClip(clip)
    }

    final override fun mount(parent: Node?) {
        this.parent = parent
    }

    final override fun unmount() {
        this.parent = null
    }

    final override fun childAtPoint(point: Point): Node? = null

    abstract fun render(g: Graphics2D, w: Double, h: Double)
}