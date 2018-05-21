package com.github.ashlanderr.kotlin.ui.core

inline fun children(builder: MutableList<Node>.() -> Unit) = ArrayList<Node>().apply(builder)
fun children(vararg items: Node) = items.toMutableList()
fun children(vararg items: Collection<Node>) = items.flatMapTo(mutableListOf()) { it }
fun children(a: Node, rest: Collection<Node>) = (listOf(a) + rest).toMutableList()
fun children(a: Node, b: Node, rest: Collection<Node>) = (listOf(a, b) + rest).toMutableList()
fun children(a: Node, b: Node, c: Node, rest: Collection<Node>) = (listOf(a, b, c) + rest).toMutableList()
fun children(a: Node, b: Node, c: Node, d: Node, rest: Collection<Node>) = (listOf(a, b, c, d) + rest).toMutableList()
fun children(a: Node, b: Node, c: Node, d: Node, e: Node, rest: Collection<Node>) = (listOf(a, b, c, d, e) + rest).toMutableList()