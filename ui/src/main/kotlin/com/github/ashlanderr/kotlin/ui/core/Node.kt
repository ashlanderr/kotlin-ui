package com.github.ashlanderr.kotlin.ui.core

import java.awt.Graphics2D

interface Node {
    val renderLeft: Double
    val renderTop: Double
    val renderWidth: Double
    val renderHeight: Double
    val parent: Node?
    val key: Any?

    fun measure(g: Graphics2D, w: Constraint, h: Constraint)
    fun arrange(left: Double, top: Double)
    fun render(g: Graphics2D)

    fun mount(parent: Node?)
    fun unmount()

    fun childAtPoint(point: Point): Node?
}

fun Node.containsPoint(point: Point): Boolean {
    return point.x >= renderLeft && point.y >= renderTop && point.x <= renderLeft + renderWidth && point.y <= renderTop + renderHeight
}

fun List<Node>.childAtPoint(point: Point): Node? {
    for (child in this) {
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