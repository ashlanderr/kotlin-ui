package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics2D
import java.awt.geom.AffineTransform

class AspectRatio(
    var ratio: Double,
    @RxNode var child: Node,
    key: Any? = null
) : AbstractNode(key) {

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

    override fun arrange(transform: AffineTransform) {
        renderTransform = transform
        child.arrange(AffineTransform())
    }

    override fun render(g: Graphics2D) = renderChildren(g) {
        clip(g) {
            child.render(g)
        }
    }

    override fun children() = listOf(child)
}