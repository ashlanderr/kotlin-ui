package com.github.ashlanderr.kotlin.ui.graphics

import com.github.ashlanderr.kotlin.ui.core.AbstractNode
import com.github.ashlanderr.kotlin.ui.core.Constraint
import com.github.ashlanderr.kotlin.ui.core.withTransform
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.AffineTransform

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

    override fun arrange(transform: AffineTransform) {
        renderTransform = transform
    }

    override fun render(g: Graphics2D) = g.withTransform(renderTransform) {
        if (fill != null) {
            g.color = fill
            g.fillRect(0, 0, renderWidth.toInt(), renderHeight.toInt())
        }
        if (stroke != null) {
            g.color = stroke
            g.drawRect(0, 0, renderWidth.toInt() - 1, renderHeight.toInt() - 1)
        }
    }
}