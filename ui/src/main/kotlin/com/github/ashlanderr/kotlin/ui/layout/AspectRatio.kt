package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics

class AspectRatio : AbstractNode() {
    @ReactiveProperty
    var ratio = 1.0

    @ReactiveProperty
    var child: Node = EmptyNode

    override fun measure(g: Graphics, maxWidth: Double, maxHeight: Double) {
        val ratioWidth = maxHeight * ratio
        val ratioHeight = maxWidth / ratio

        if (ratioWidth > maxWidth) {
            renderWidth = maxWidth
            renderHeight = ratioHeight
        } else {
            renderWidth = ratioWidth
            renderHeight = maxHeight
        }

        child.measure(g, renderWidth, renderHeight)
        renderWidth = child.renderWidth
        renderHeight = child.renderHeight
    }

    override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top
        child.arrange(left, top)
    }

    override fun render(g: Graphics) {
        child.render(g)
    }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }
}

fun aspectRatio(builder: Builder<AspectRatio>) = build(builder)