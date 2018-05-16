package com.github.ashlanderr.layout

import com.github.ashlanderr.core.*
import java.awt.Graphics

class ListView : Node, Parent {
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

    @ReactiveList
    override val children: MutableList<Node> = ArrayList()

    override fun measure(g: Graphics, maxWidth: Double, maxHeight: Double) {
        renderWidth = maxWidth
        renderHeight = 0.0

        for (child in children) {
            child.measure(g, maxWidth, Double.POSITIVE_INFINITY)
            renderHeight += child.renderHeight
        }

        renderHeight = maxHeight
    }

    override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top

        var childTop = top
        for (child in children) {
            child.arrange(left, childTop)
            childTop += child.renderHeight
        }
    }

    override fun render(g: Graphics) {
        val clip = g.pushClip(renderLeft.toInt(), renderTop.toInt(), renderWidth.toInt(), renderHeight.toInt())

        for (child in children) {
            child.render(g)
        }

        g.popClip(clip)
    }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }
}

fun listView(builder: Builder<ListView>) = build(builder)