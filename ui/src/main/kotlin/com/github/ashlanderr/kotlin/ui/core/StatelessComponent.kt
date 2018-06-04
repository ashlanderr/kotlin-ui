package com.github.ashlanderr.kotlin.ui.core

import java.awt.Graphics2D
import java.awt.geom.AffineTransform

abstract class StatelessComponent(final override var key: Any? = null) : Node {
    final override var renderTransform = AffineTransform()
        private set
    final override val renderWidth get() = child.renderWidth
    final override val renderHeight get() = child.renderHeight

    final override var parent: Node? = null
        private set

    private var child: Node = EmptyNode

    final override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        renderAndMerge()
        child.measure(g, w, h)
    }

    final override fun arrange(transform: AffineTransform) {
        renderTransform = transform
        child.arrange(AffineTransform())
    }

    final override fun render(g: Graphics2D) = renderChildren(g) {
        child.render(g)
    }

    @Suppress("UNCHECKED_CAST")
    final override fun mount(parent: Node?) {
        this.parent = parent
        renderAndMerge()
    }

    final override fun unmount() {
        child = MergeProcessor.merge(child, EmptyNode)
    }

    final override fun children() = listOf(child)
    final override fun parentToLocal(point: Point) = point

    abstract fun render(): Node

    private fun renderAndMerge() {
        val newChild = render()
        child = MergeProcessor.merge(child, newChild, this)
    }
}