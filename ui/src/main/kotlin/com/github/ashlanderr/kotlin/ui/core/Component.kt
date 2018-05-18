package com.github.ashlanderr.kotlin.ui.core

import java.awt.Graphics

abstract class Component<S : State<S, C>, C: Component<S, C>> : Node {
    override val renderLeft get() = child.renderLeft
    override val renderTop get() = child.renderTop
    override val renderWidth get() = child.renderWidth
    override val renderHeight get() = child.renderHeight

    final override var parent: Node? = null
        private set

    lateinit var state: S
        private set

    private var child: Node = EmptyNode

    override fun measure(g: Graphics, w: Constraint, h: Constraint) {
        child.measure(g, w, h)
    }

    override fun arrange(left: Double, top: Double) {
        child.arrange(left, top)
    }

    override fun render(g: Graphics) {
        renderState()
        child.render(g)
    }

    @Suppress("UNCHECKED_CAST")
    override fun mount(parent: Node?) {
        this.parent = parent
        state = initState()
        state.component = this as C
        renderState()
    }

    override fun unmount() {
        child = MergeProcessor.merge(child, EmptyNode)
        state.dispose()
    }

    override fun childAtPoint(point: Point): Node? {
        return child.childAtPoint(point)
    }

    protected abstract fun initState(): S

    private fun renderState() {
        val newChild = state.render()
        child = MergeProcessor.merge(child, newChild, this)
    }
}