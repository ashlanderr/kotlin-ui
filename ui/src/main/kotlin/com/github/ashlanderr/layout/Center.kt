package com.github.ashlanderr.layout

import com.github.ashlanderr.core.*
import java.awt.Graphics

class Center : Node {
    override var renderLeft: Double = 0.0
        private set

    override var renderTop: Double = 0.0
        private set

    override var renderWidth: Double = 0.0
        private set

    override var renderHeight: Double = 0.0
        private set

    override var parent: Node? = null
        private set

    @ReactiveProperty
    var child: Node = EmptyNode

    override fun measure(g: Graphics, maxWidth: Double, maxHeight: Double) {
        child.measure(g, maxWidth, maxHeight)
        renderWidth = maxWidth
        renderHeight = maxHeight
    }

    override fun arrange(left: Double, top: Double) {
        val childLeft = left + (renderWidth - child.renderWidth) / 2
        val childTop = top + (renderHeight - child.renderHeight) / 2
        renderLeft = left
        renderTop = top
        child.arrange(childLeft, childTop)
    }

    override fun render(g: Graphics) {
        val clip = g.pushClip(renderLeft.toInt(), renderTop.toInt(), renderWidth.toInt(), renderHeight.toInt())

        child.render(g)

        g.popClip(clip)
    }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }
}

fun center(builder: Builder<Center>) = build(builder)