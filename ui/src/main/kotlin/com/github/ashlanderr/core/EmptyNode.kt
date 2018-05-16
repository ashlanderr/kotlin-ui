package com.github.ashlanderr.core

import java.awt.Graphics

object EmptyNode : Node {
    override val renderLeft: Double = 0.0
    override val renderTop: Double = 0.0
    override val renderWidth: Double = 0.0
    override val renderHeight: Double = 0.0
    override val parent: Node? = null

    override fun measure(g: Graphics, maxWidth: Double, maxHeight: Double) { }
    override fun arrange(left: Double, top: Double) { }
    override fun render(g: Graphics) { }
    override fun mount(parent: Node?) { }
    override fun unmount() { }
}