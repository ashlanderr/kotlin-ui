package com.github.ashlanderr.kotlin.ui.core

import java.awt.Graphics

abstract class AbstractNode : Node {
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

    override fun measure(g: Graphics, maxWidth: Double, maxHeight: Double) { }
    override fun arrange(left: Double, top: Double) { }
    override fun render(g: Graphics) { }
    override fun mount(parent: Node?) { }
    override fun unmount() { }
}