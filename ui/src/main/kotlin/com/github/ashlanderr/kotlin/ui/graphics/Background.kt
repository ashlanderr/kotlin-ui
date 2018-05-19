package com.github.ashlanderr.kotlin.ui.graphics

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Color
import java.awt.Graphics

class Background : AbstractNode() {
    @ReactiveNode
    var child: Node = EmptyNode

    @ReactiveProperty
    var color: Color = Color.WHITE

    override fun measure(g: Graphics, w: Constraint, h: Constraint) {
        child.measure(g, w, h)
        renderWidth = child.renderWidth
        renderHeight = child.renderHeight
    }

    override fun arrange(left: Double, top: Double) {
        child.arrange(left, top)
        renderLeft = left
        renderTop = top
    }

    override fun render(g: Graphics) {
        g.color = this.color
        g.fillRect(renderLeft.toInt(), renderTop.toInt(), renderWidth.toInt(), renderHeight.toInt())
        child.render(g)
    }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }

    override fun childAtPoint(point: Point) = child.takeIf { it.containsPoint(point) }
}

fun background(builder: Builder<Background>) = build(builder)