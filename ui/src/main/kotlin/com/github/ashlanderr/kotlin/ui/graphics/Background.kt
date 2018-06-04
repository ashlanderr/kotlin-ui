package com.github.ashlanderr.kotlin.ui.graphics

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.AffineTransform

class Background(
    @RxNode var child: Node,
    var color: Color,
    key: Any? = null
) : AbstractNode(key) {

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        child.measure(g, w, h)
        renderWidth = child.renderWidth
        renderHeight = child.renderHeight
    }

    override fun arrange(transform: AffineTransform) {
        child.arrange(AffineTransform())
        renderTransform = transform
    }

    override fun render(g: Graphics2D) = renderChildren(g) {
        g.color = this.color
        g.fillRect(0, 0, renderWidth.toInt(), renderHeight.toInt())
        child.render(g)
    }

    override fun children() = listOf(child)
}