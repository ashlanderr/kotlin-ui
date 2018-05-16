package com.github.ashlanderr.layout

import com.github.ashlanderr.core.*
import java.awt.Graphics
import kotlin.math.max
import kotlin.math.min

class ConstrainedBox : Node {
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
    var maxWidth: Double = Double.POSITIVE_INFINITY

    @ReactiveProperty
    var maxHeight: Double = Double.POSITIVE_INFINITY

    @ReactiveProperty
    var minWidth: Double = 0.0

    @ReactiveProperty
    var minHeight: Double = 0.0

    @ReactiveProperty
    var child: Node = EmptyNode

    override fun measure(g: Graphics, maxWidth: Double, maxHeight: Double) {
        val width = max(this.minWidth, min(this.maxWidth, maxWidth))
        val height = max(this.minHeight, min(this.maxHeight, maxHeight))
        child.measure(g, width, height)
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

fun constrainedBox(builder: Builder<ConstrainedBox>) = build(builder)