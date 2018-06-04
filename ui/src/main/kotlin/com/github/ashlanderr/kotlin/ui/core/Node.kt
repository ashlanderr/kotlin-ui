package com.github.ashlanderr.kotlin.ui.core

import java.awt.Graphics2D
import java.awt.geom.AffineTransform

interface Node {
    val renderTransform: AffineTransform
    val renderWidth: Double
    val renderHeight: Double
    val parent: Node?
    val key: Any?

    fun measure(g: Graphics2D, w: Constraint, h: Constraint)
    fun arrange(transform: AffineTransform)
    fun render(g: Graphics2D)

    fun mount(parent: Node?)
    fun unmount()

    fun children(): List<Node>
    fun parentToLocal(point: Point): Point
}

fun Node.containsPoint(point: Point): Boolean {
    val childPoint = this.renderTransform.createInverse().transform(point)
    return childPoint.x >= 0 && childPoint.y >= 0 && childPoint.x <= renderWidth && childPoint.y <= renderHeight
}

fun List<Node>.childAtPoint(point: Point): Node? {
    for (child in this.asReversed()) {
        if (child.containsPoint(point)) return child
    }
    return null
}

inline fun <reified T : Node> Node.ancestor(): T? {
    var parent: Node? = this
    while (parent != null && parent !is T) {
        parent = parent.parent
    }
    return parent as T?
}