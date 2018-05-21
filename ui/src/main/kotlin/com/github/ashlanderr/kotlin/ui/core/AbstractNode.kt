package com.github.ashlanderr.kotlin.ui.core

import java.awt.Graphics2D

abstract class AbstractNode(@ReactiveProperty override var key: Any?) : Node {
    override var renderLeft: Double = 0.0
        protected set

    override var renderTop: Double = 0.0
        protected set

    override var renderWidth: Double = 0.0
        protected set

    override var renderHeight: Double = 0.0
        protected set

    override var parent: Node? = null
        protected set

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {}
    override fun arrange(left: Double, top: Double) {}
    override fun render(g: Graphics2D) {}
    override fun mount(parent: Node?) {}
    override fun unmount() {}
    override fun childAtPoint(point: Point): Node? = null
}