package com.github.ashlanderr.kotlin.ui.core

import java.awt.Graphics2D
import java.awt.geom.AffineTransform

inline fun children(builder: MutableList<Node>.() -> Unit) = ArrayList<Node>().apply(builder)
fun children(vararg items: Node) = items.toMutableList()
fun children(vararg items: Collection<Node>) = items.flatMapTo(mutableListOf()) { it }
fun children(a: Node, rest: Collection<Node>) = (listOf(a) + rest).toMutableList()
fun children(a: Node, b: Node, rest: Collection<Node>) = (listOf(a, b) + rest).toMutableList()
fun children(a: Node, b: Node, c: Node, rest: Collection<Node>) = (listOf(a, b, c) + rest).toMutableList()
fun children(a: Node, b: Node, c: Node, d: Node, rest: Collection<Node>) = (listOf(a, b, c, d) + rest).toMutableList()
fun children(a: Node, b: Node, c: Node, d: Node, e: Node, rest: Collection<Node>) = (listOf(a, b, c, d, e) + rest).toMutableList()

inline fun Node.renderChildren(g: Graphics2D, block: () -> Unit) {
    val transform = g.transform
    g.transform(this.renderTransform)
    block()
    g.transform = transform
}

inline fun Node.clip(g: Graphics2D, region: Node? = null, block: () -> Unit) {
    val transform = region?.renderTransform ?: AffineTransform()
    val (left, top) = transform.transform(Point.ZERO)

    val clip = g.pushClip(
        left.toInt(),
        top.toInt(),
        (region?.renderWidth ?: this.renderWidth).toInt(),
        (region?.renderHeight ?: this.renderHeight).toInt()
    )
    block()
    g.popClip(clip)
}