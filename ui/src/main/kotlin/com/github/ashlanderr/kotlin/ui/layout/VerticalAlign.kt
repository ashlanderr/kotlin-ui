package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.Constraint

sealed class VerticalAlign {
    abstract fun computeTop(childHeight: Double, maxHeight: Double): Double
    abstract fun computeHeight(selfHeight: Double, parentHeight: Constraint): Constraint

    object TOP : VerticalAlign() {
        override fun computeTop(childHeight: Double, maxHeight: Double) = 0.0
        override fun computeHeight(selfHeight: Double, parentHeight: Constraint) = Constraint.Min(parentHeight.compute(selfHeight, parentHeight.size))
    }

    object CENTER : VerticalAlign() {
        override fun computeTop(childHeight: Double, maxHeight: Double) = (maxHeight - childHeight) / 2.0
        override fun computeHeight(selfHeight: Double, parentHeight: Constraint) = Constraint.Min(parentHeight.compute(selfHeight, parentHeight.size))
    }

    object BOTTOM : VerticalAlign() {
        override fun computeTop(childHeight: Double, maxHeight: Double) = maxHeight - childHeight
        override fun computeHeight(selfHeight: Double, parentHeight: Constraint) = Constraint.Min(parentHeight.compute(selfHeight, parentHeight.size))
    }

    object STRETCH : VerticalAlign() {
        override fun computeTop(childHeight: Double, maxHeight: Double) = 0.0
        override fun computeHeight(selfHeight: Double, parentHeight: Constraint) = Constraint.Max(parentHeight.compute(selfHeight, parentHeight.size))
    }
}