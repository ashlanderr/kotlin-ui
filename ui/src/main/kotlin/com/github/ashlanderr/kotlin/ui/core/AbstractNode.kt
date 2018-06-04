package com.github.ashlanderr.kotlin.ui.core

import java.awt.Graphics2D
import java.awt.geom.AffineTransform

abstract class AbstractNode(override var key: Any?) : Node {
    override var renderTransform = AffineTransform()
        protected set

    override var renderWidth: Double = 0.0
        protected set

    override var renderHeight: Double = 0.0
        protected set

    final override var parent: Node? = null
        private set

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {}
    override fun arrange(transform: AffineTransform) {}
    override fun render(g: Graphics2D) {}

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }

    override fun children() = emptyList<Node>()

    override fun parentToLocal(point: Point) = renderTransform.transform(point)
}