package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics

data class Indent(val left: Double, val top: Double, val right: Double, val bottom: Double) {
    companion object {
        val ZERO = all(0.0)

        fun symetric(horizontal: Double, vertical: Double) = Indent(horizontal, vertical, horizontal, vertical)
        fun all(all: Double) = Indent(all, all, all, all)
    }

}

class Padding : AbstractNode() {
    @ReactiveProperty
    var padding: Indent = Indent.ZERO

    @ReactiveProperty
    var child: Node = EmptyNode

    override fun measure(g: Graphics, w: Constraint, h: Constraint) {
        val horizontalPadding = padding.left + padding.right
        val verticalPadding = padding.top + padding.bottom
        val cw = w.copy(w.size - horizontalPadding)
        val ch = h.copy(h.size - verticalPadding)

        child.measure(g, cw, ch)

        renderWidth = child.renderWidth + horizontalPadding
        renderHeight = child.renderHeight + verticalPadding
    }

    override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top
        child.arrange(left + padding.left, top + padding.top)
    }

    override fun render(g: Graphics) {
        child.render(g)
    }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }

    override fun childAtPoint(point: Point) = child.takeIf { it.containsPoint(point) }
}

fun padding(builder: Builder<Padding>) = build(builder)