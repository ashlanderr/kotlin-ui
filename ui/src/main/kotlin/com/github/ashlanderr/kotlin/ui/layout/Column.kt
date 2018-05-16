package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics
import kotlin.math.max
import kotlin.math.min

class Column : AbstractNode(), Parent {
    @ReactiveList
    override val children: MutableList<Node> = ArrayList()

    @ReactiveProperty
    var align: HorizontalAlign = HorizontalAlign.LEFT

    override fun measure(g: Graphics, maxWidth: Double, maxHeight: Double) {
        renderWidth = align.minWidth
        renderHeight = 0.0

        for (child in children) {
            child.measure(g, maxWidth, maxHeight - renderHeight)
            renderWidth = max(renderWidth, child.renderWidth)
            renderHeight += child.renderHeight
        }

        renderWidth = min(renderWidth, maxWidth)
        renderHeight = min(renderHeight, maxHeight)
    }

    override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top
        align.arrange(this, children, left, top)
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

fun column(builder: Builder<Column>) = build(builder)