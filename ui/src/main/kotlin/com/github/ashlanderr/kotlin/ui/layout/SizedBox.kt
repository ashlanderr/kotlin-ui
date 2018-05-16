package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics

class SizedBox : AbstractNode() {
    @ReactiveProperty
    var width: Double = 0.0

    @ReactiveProperty
    var height: Double = 0.0

    override fun measure(g: Graphics, maxWidth: Double, maxHeight: Double) {
        renderWidth = width
        renderHeight = height
    }

    override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top
    }

    override fun render(g: Graphics) { }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }
}

fun sizedBox(builder: Builder<SizedBox>) = build(builder)