package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.Node

sealed class HorizontalAlign {
    abstract val minWidth: Double
    abstract fun arrange(root: Column, children: List<Node>, left: Double, top: Double)

    object LEFT : HorizontalAlign() {
        override val minWidth: Double = 0.0

        override fun arrange(root: Column, children: List<Node>, left: Double, top: Double) {
            var childTop = top
            for (child in children) {
                child.arrange(left, childTop)
                childTop += child.renderHeight
            }
        }
    }

    object CENTER : HorizontalAlign() {
        override val minWidth: Double = 0.0

        override fun arrange(root: Column, children: List<Node>, left: Double, top: Double) {
            var childTop = top
            for (child in children) {
                val childLeft = left + (root.renderWidth - child.renderWidth) / 2
                child.arrange(childLeft, childTop)
                childTop += child.renderHeight
            }
        }
    }

    object RIGHT : HorizontalAlign() {
        override val minWidth: Double = 0.0

        override fun arrange(root: Column, children: List<Node>, left: Double, top: Double) {
            var childTop = top
            for (child in children) {
                val childLeft = left + root.renderWidth - child.renderWidth
                child.arrange(childLeft, childTop)
                childTop += child.renderHeight
            }
        }
    }

    object STRETCH : HorizontalAlign() {
        override val minWidth: Double = Double.POSITIVE_INFINITY

        override fun arrange(root: Column, children: List<Node>, left: Double, top: Double) {
            var childTop = top
            for (child in children) {
                child.arrange(left, childTop)
                childTop += child.renderHeight
            }
        }
    }
}