package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import kotlin.math.min

class FitBox(
    @RxNode var child: Node,
    key: Any? = null
) : AbstractNode(key) {
    private var scale: Double = 1.0

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        renderWidth = w.compute(0.0, w.size)
        renderHeight = h.compute(0.0, h.size)
        child.measure(g, Constraint.Min(Double.POSITIVE_INFINITY), Constraint.Min(Double.POSITIVE_INFINITY))

        scale = min(renderWidth / child.renderWidth, renderHeight / child.renderHeight)
        child.measure(g, Constraint.Max(renderWidth / scale), Constraint.Max(renderHeight / scale))
    }

    override fun arrange(transform: AffineTransform) {
        renderTransform = transform
        child.arrange(AffineTransform().apply {
            scale(scale, scale)
        })
    }

    override fun render(g: Graphics2D) = renderChildren(g) {
        child.render(g)
    }

    override fun children() = listOf(child)
}