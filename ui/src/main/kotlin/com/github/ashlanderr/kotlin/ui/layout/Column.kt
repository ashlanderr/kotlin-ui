package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import kotlin.math.max

class Column(
    var align: HorizontalAlign = HorizontalAlign.LEFT,
    @RxList override var children: MutableList<Node>,
    key: Any? = null
) : AbstractNode(key), Parent {

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
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

    override fun arrange(transform: AffineTransform) {
        renderTransform = transform
        var childTop = 0.0

        for (child in children) {
            val childLeft = align.computeLeft(child.renderWidth, renderWidth)
            child.arrange(AffineTransform().apply {
                translate(childLeft, childTop)
            })
            childTop += child.renderHeight
        }
    }

    override fun render(g: Graphics2D) = renderChildren(g) {
        for (child in children) {
            clip(g, child) {
                child.render(g)
            }
        }
    }

    override fun children() = children
}