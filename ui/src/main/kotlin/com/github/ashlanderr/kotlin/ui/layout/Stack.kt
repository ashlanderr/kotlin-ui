package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics2D
import kotlin.math.max

class Stack(
    @RxList override var children: MutableList<Node>,
    key: Any? = null
) : AbstractNode(key), Parent {

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        var cw: Constraint = Constraint.Min(w.size)
        var ch: Constraint = Constraint.Min(h.size)

        var width = 0.0
        var height = 0.0

        for (child in children) {
            child.measure(g, cw, ch)
            width = max(width, child.renderWidth)
            height = max(height, child.renderHeight)
        }

        cw = Constraint.Max(w.compute(width, w.size))
        ch = Constraint.Max(h.compute(height, h.size))

        renderWidth = 0.0
        renderHeight = 0.0

        for (child in children) {
            child.measure(g, cw, ch)
            renderWidth = max(renderWidth, child.renderWidth)
            renderHeight = max(renderHeight, child.renderHeight)
        }
    }

    override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top

        for (child in children) {
            child.arrange(left, top)
        }
    }

    override fun render(g: Graphics2D) {
        for (child in children) {
            child.render(g)
        }
    }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }

    override fun childAtPoint(point: Point) = children.asReversed().childAtPoint(point)
}