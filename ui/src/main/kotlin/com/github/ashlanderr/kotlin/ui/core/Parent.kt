package com.github.ashlanderr.kotlin.ui.core

interface Parent {
    val children: MutableList<Node>

    operator fun Node.unaryPlus() {
        children.add(this)
    }
}