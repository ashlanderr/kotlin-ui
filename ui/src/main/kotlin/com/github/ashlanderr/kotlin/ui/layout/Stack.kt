package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
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

    override fun arrange(transform: AffineTransform) {
        renderTransform = transform

        for (child in children) {
            child.arrange(AffineTransform())
        }
    }

    override fun render(g: Graphics2D) = renderChildren(g) {
        for (child in children) {
            child.render(g)
        }
    }

    override fun children() = children
}