package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import kotlin.math.max
import kotlin.math.min

class ConstrainedBox(
    var maxWidth: Double = Double.POSITIVE_INFINITY,
    var maxHeight: Double = Double.POSITIVE_INFINITY,
    var minWidth: Double = 0.0,
    var minHeight: Double = 0.0,
    @RxNode var child: Node,
    key: Any? = null
) : AbstractNode(key) {

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        renderWidth = max(minWidth, min(maxWidth, w.size))
        renderHeight = max(minHeight, min(maxHeight, h.size))
        child.measure(g, Constraint.Max(renderWidth), Constraint.Max(renderHeight))
        renderWidth = child.renderWidth
        renderHeight = child.renderHeight
    }

    override fun arrange(transform: AffineTransform) {
        renderTransform = transform
        child.arrange(AffineTransform())
    }

    override fun render(g: Graphics2D) = renderChildren(g) {
        child.render(g)
    }

    override fun children() = listOf(child)
}