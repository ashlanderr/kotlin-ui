package com.github.ashlanderr.core

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

    override fun measure(g: Graphics, maxWidth: Double, maxHeight: Double) {
        child.measure(g, maxWidth, maxHeight)
    }

    override fun arrange(left: Double, top: Double) {
        child.arrange(left, top)
    }

    override fun render(g: Graphics) {
        val newChild = state.render()
        child = MergeProcessor.merge(child, newChild, this)
        child.render(g)
    }

    @Suppress("UNCHECKED_CAST")
    override fun mount(parent: Node?) {
        this.parent = parent
        state = initState()
        state.component = this as C
    }

    override fun unmount() {
        child = MergeProcessor.merge(child, EmptyNode)
        state.dispose()
    }

    protected abstract fun initState(): S
}