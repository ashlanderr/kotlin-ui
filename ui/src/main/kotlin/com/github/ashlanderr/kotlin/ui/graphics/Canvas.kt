package com.github.ashlanderr.kotlin.ui.graphics

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics2D
import java.awt.geom.AffineTransform

abstract class Canvas(key: Any? = null) : Node {
    final override var renderTransform = AffineTransform()
        private set

    final override var renderWidth: Double = 0.0
        private set

    final override var renderHeight: Double = 0.0
        private set

    final override var parent: Node? = null
        private set

    final override var key: Any? = key
        private set

    final override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        renderWidth = w.compute(0.0, w.size)
        renderHeight = h.compute(0.0, h.size)
    }

    final override fun arrange(transform: AffineTransform) {
        renderTransform = transform
    }

    final override fun render(g: Graphics2D) = renderChildren(g) {
        clip(g) {
            render(g, renderWidth, renderHeight)
        }
    }

    final override fun mount(parent: Node?) {
        this.parent = parent
    }

    final override fun unmount() {
        this.parent = null
    }

    final override fun children() = emptyList<Node>()
    final override fun parentToLocal(point: Point) = point

    abstract fun render(g: Graphics2D, w: Double, h: Double)
}