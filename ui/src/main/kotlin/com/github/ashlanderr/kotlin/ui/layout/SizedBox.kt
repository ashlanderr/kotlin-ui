package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.AbstractNode
import com.github.ashlanderr.kotlin.ui.core.Constraint
import com.github.ashlanderr.kotlin.ui.core.Node
import com.github.ashlanderr.kotlin.ui.core.ReactiveProperty
import java.awt.Graphics2D

class SizedBox(
    @ReactiveProperty var width: Double,
    @ReactiveProperty var height: Double,
    key: Any? = null
) : AbstractNode(key) {

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        renderWidth = width
        renderHeight = height
    }

    override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top
    }

    override fun render(g: Graphics2D) {}

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }
}