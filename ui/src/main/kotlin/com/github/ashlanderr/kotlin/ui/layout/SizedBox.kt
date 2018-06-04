package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.AbstractNode
import com.github.ashlanderr.kotlin.ui.core.Constraint
import java.awt.Graphics2D
import java.awt.geom.AffineTransform

class SizedBox(
    var width: Double,
    var height: Double,
    key: Any? = null
) : AbstractNode(key) {

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        renderWidth = width
        renderHeight = height
    }

    override fun arrange(transform: AffineTransform) {
        renderTransform = transform
    }

    override fun render(g: Graphics2D) {}
}