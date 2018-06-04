package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics2D
import java.awt.geom.AffineTransform

class Center(
    @RxNode var child: Node,
    key: Any? = null
) : AbstractNode(key) {

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        child.measure(g, Constraint.Min(w.size), Constraint.Min(h.size))
        renderWidth = w.compute(child.renderWidth, w.size)
        renderHeight = h.compute(child.renderHeight, h.size)
    }

    override fun arrange(transform: AffineTransform) {
        renderTransform = transform

        val childLeft = (renderWidth - child.renderWidth) / 2
        val childTop = (renderHeight - child.renderHeight) / 2
        child.arrange(AffineTransform().apply {
            translate(childLeft, childTop)
        })
    }

    override fun render(g: Graphics2D) = renderChildren(g) {
        clip(g) {
            child.render(g)
        }
    }

    override fun children() = listOf(child)
}