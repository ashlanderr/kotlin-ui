package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics2D
import java.awt.geom.AffineTransform

data class Indent(val left: Double, val top: Double, val right: Double, val bottom: Double) {
    companion object {
        val ZERO = all(0.0)

        fun symmetric(horizontal: Double, vertical: Double) = Indent(horizontal, vertical, horizontal, vertical)
        fun all(all: Double) = Indent(all, all, all, all)
    }

}

class Padding(
    var padding: Indent,
    @RxNode var child: Node,
    key: Any? = null
) : AbstractNode(key) {

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        val horizontalPadding = padding.left + padding.right
        val verticalPadding = padding.top + padding.bottom
        val cw = w.copy(w.size - horizontalPadding)
        val ch = h.copy(h.size - verticalPadding)

        child.measure(g, cw, ch)

        renderWidth = child.renderWidth + horizontalPadding
        renderHeight = child.renderHeight + verticalPadding
    }

    override fun arrange(transform: AffineTransform) {
        renderTransform = transform
        child.arrange(AffineTransform().apply {
            translate(padding.left, padding.top)
        })
    }

    override fun render(g: Graphics2D) = renderChildren(g) {
        child.render(g)
    }

    override fun children() = listOf(child)
}