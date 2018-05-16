package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics
import kotlin.math.max
import kotlin.math.min

class ConstrainedBox : AbstractNode() {
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

    override fun measure(g: Graphics, w: Constraint, h: Constraint) {
        renderWidth = max(minWidth, min(maxWidth, w.size))
        renderHeight = max(minHeight, min(maxHeight, h.size))
        child.measure(g, Constraint.Max(renderWidth), Constraint.Max(renderHeight))
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