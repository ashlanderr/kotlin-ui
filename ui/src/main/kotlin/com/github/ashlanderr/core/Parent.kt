package com.github.ashlanderr.core

interface Parent {
    val children: MutableList<Node>

    operator fun Node.unaryPlus() {
        children.add(this)
    }
}