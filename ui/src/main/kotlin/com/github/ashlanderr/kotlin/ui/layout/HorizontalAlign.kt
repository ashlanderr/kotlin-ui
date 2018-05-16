package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.Constraint

sealed class HorizontalAlign {
    abstract fun computeLeft(childWidth: Double, maxWidth: Double): Double
    abstract fun computeWidth(selfWidth: Double, parentWidth: Constraint): Constraint

    object LEFT : HorizontalAlign() {
        override fun computeLeft(childWidth: Double, maxWidth: Double) = 0.0
        override fun computeWidth(selfWidth: Double, parentWidth: Constraint) = Constraint.Min(parentWidth.compute(selfWidth, parentWidth.size))
    }

    object CENTER : HorizontalAlign() {
        override fun computeLeft(childWidth: Double, maxWidth: Double) = (maxWidth - childWidth) / 2.0
        override fun computeWidth(selfWidth: Double, parentWidth: Constraint) = Constraint.Min(parentWidth.compute(selfWidth, parentWidth.size))
    }

    object RIGHT : HorizontalAlign() {
        override fun computeLeft(childWidth: Double, maxWidth: Double) = maxWidth - childWidth
        override fun computeWidth(selfWidth: Double, parentWidth: Constraint) = Constraint.Min(parentWidth.compute(selfWidth, parentWidth.size))
    }

    object STRETCH : HorizontalAlign() {
        override fun computeLeft(childWidth: Double, maxWidth: Double) = 0.0
        override fun computeWidth(selfWidth: Double, parentWidth: Constraint) = Constraint.Max(parentWidth.compute(selfWidth, parentWidth.size))
    }
}