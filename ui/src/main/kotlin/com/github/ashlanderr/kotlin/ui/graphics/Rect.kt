package com.github.ashlanderr.kotlin.ui.graphics

import com.github.ashlanderr.kotlin.ui.core.AbstractNode
import com.github.ashlanderr.kotlin.ui.core.Constraint
import com.github.ashlanderr.kotlin.ui.core.Node
import java.awt.Color
import java.awt.Graphics2D

class Rect(
    var width: Double? = null,
    var height: Double? = null,
    var fill: Color? = Color.WHITE,
    var stroke: Color? = Color.BLACK,
    key: Any? = null
) : AbstractNode(key) {

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        renderWidth = width ?: w.compute(0.0, w.size)
        renderHeight = height ?: h.compute(0.0, h.size)
    }

    override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top
    }

    override fun render(g: Graphics2D) {
        if (fill != null) {
            g.color = fill
            g.fillRect(renderLeft.toInt(), renderTop.toInt(), renderWidth.toInt(), renderHeight.toInt())
        }
        if (stroke != null) {
            g.color = stroke
            g.drawRect(renderLeft.toInt(), renderTop.toInt(), renderWidth.toInt() - 1, renderHeight.toInt() - 1)
        }
    }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }
}