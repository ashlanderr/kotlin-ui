package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics
import kotlin.math.max

class Column : AbstractNode(), Parent {
    @ReactiveList
    override val children: MutableList<Node> = ArrayList()

    @ReactiveProperty
    var align: HorizontalAlign = HorizontalAlign.LEFT

    override fun measure(g: Graphics, w: Constraint, h: Constraint) {
        renderWidth = 0.0
        val ch = Constraint.Min(Double.POSITIVE_INFINITY)
        var cw: Constraint = Constraint.Min(w.size)

        for (child in children) {
            child.measure(g, cw, ch)
            renderWidth = max(renderWidth, child.renderWidth)
        }

        cw = align.computeWidth(renderWidth, w)
        renderWidth = cw.size
        renderHeight = 0.0

        for (child in children) {
            child.measure(g, cw, ch)
            renderHeight += child.renderHeight
        }
    }

    override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top
        var childTop = top

        for (child in children) {
            val childLeft = align.computeLeft(child.renderWidth, renderWidth) + renderLeft
            child.arrange(childLeft, childTop)
            childTop += child.renderHeight
        }
    }

    override fun render(g: Graphics) {
        val clip = g.pushClip(renderLeft.toInt(), renderTop.toInt(), renderWidth.toInt(), renderHeight.toInt())

        for (child in children) {
            child.render(g)
        }

        g.popClip(clip)
    }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }

    override fun childAtPoint(point: Point) = children.childAtPoint(point)
}

fun column(builder: Builder<Column>) = build(builder)