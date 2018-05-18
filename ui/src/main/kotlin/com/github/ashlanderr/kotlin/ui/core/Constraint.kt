package com.github.ashlanderr.kotlin.ui.core

interface Constraint {
    val size: Double
    fun compute(min: Double, max: Double): Double
    fun copy(size: Double): Constraint

    class Min(override val size: Double) : Constraint {
        override fun compute(min: Double, max: Double) = min
        override fun copy(size: Double) = Min(size)

        override fun toString(): String {
            return "Constraint.Min(size=$size)"
        }
    }

    class Max(override val size: Double) : Constraint {
        override fun compute(min: Double, max: Double) = max
        override fun copy(size: Double) = Max(size)

        override fun toString(): String {
            return "Constraint.Max(size=$size)"
        }
    }
}