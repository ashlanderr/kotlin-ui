package com.github.ashlanderr.kotlin.ui.core

import java.awt.Graphics2D
import java.awt.geom.AffineTransform

abstract class StatefulComponent<S : State, C : StatefulComponent<S, C>>(final override var key: Any? = null) : Node {
    final override var renderTransform = AffineTransform()
        private set
    final override val renderWidth get() = child.renderWidth
    final override val renderHeight get() = child.renderHeight

    final override var parent: Node? = null
        private set

    lateinit var state: S
        private set

    private var child: Node = EmptyNode

    final override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        renderState()
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
        state = initState(this as C)
        renderState()
    }

    final override fun unmount() {
        child = MergeProcessor.merge(child, EmptyNode)
        state.dispose()
    }

    final override fun parentToLocal(point: Point) = point
    final override fun children() = listOf(child)

    protected abstract fun initState(component: C): S

    private fun renderState() {
        val newChild = state.render()
        child = MergeProcessor.merge(child, newChild, this)
    }
}