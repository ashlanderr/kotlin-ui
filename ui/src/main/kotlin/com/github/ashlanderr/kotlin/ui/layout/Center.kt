package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics2D

class Center(
    @RxNode var child: Node,
    key: Any? = null
) : AbstractNode(key) {

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        child.measure(g, Constraint.Min(w.size), Constraint.Min(h.size))
        renderWidth = w.compute(child.renderWidth, w.size)
        renderHeight = h.compute(child.renderHeight, h.size)
    }

    override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top

        val childLeft = left + (renderWidth - child.renderWidth) / 2
        val childTop = top + (renderHeight - child.renderHeight) / 2
        child.arrange(childLeft, childTop)
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