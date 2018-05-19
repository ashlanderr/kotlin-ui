package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics2D

class AspectRatio : AbstractNode() {
    @ReactiveProperty
    var ratio = 1.0

    @ReactiveNode
    var child: Node = EmptyNode

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        val maxWidth = w.compute(0.0, w.size)
        val maxHeight = h.compute(0.0, h.size)
        val ratioWidth = maxHeight * ratio
        val ratioHeight = maxWidth / ratio

        if (ratioHeight < h.size) {
            renderWidth = maxWidth
            renderHeight = ratioHeight
        } else {
            renderWidth = ratioWidth
            renderHeight = maxHeight
        }

        child.measure(g, Constraint.Max(renderWidth), Constraint.Max(renderHeight))
    }

    override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top
        child.arrange(left, top)
    }

    override fun render(g: Graphics2D) {
        val clip = g.pushClip(renderLeft.toInt(), renderTop.toInt(), renderWidth.toInt(), renderHeight.toInt())

        child.render(g)

        g.popClip(clip)
    }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }

    override fun childAtPoint(point: Point) = child.takeIf { child.containsPoint(point) }
}

fun aspectRatio(builder: Builder<AspectRatio>) = build(builder)