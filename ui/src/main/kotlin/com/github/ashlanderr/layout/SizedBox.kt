package com.github.ashlanderr.layout

import com.github.ashlanderr.core.Builder
import com.github.ashlanderr.core.Node
import com.github.ashlanderr.core.ReactiveProperty
import com.github.ashlanderr.core.build
import java.awt.Graphics

class SizedBox : Node {
    override var renderLeft: Double = 0.0
        private set

    override var renderTop: Double = 0.0
        private set

    override var parent: Node? = null
        private set

    override val renderWidth: Double get() = width
    override val renderHeight: Double get() = height

    @ReactiveProperty
    var width: Double = 0.0

    @ReactiveProperty
    var height: Double = 0.0

    override fun measure(g: Graphics, maxWidth: Double, maxHeight: Double) { }
    override fun arrange(left: Double, top: Double) { }
    override fun render(g: Graphics) { }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }
}

fun sizedBox(builder: Builder<SizedBox>) = build(builder)