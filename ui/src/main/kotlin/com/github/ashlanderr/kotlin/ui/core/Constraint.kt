package com.github.ashlanderr.kotlin.ui.core

interface Constraint {
    val size: Double
    fun compute(min: Double, max: Double): Double

    class Min(override val size: Double) : Constraint {
        override fun compute(min: Double, max: Double) = min
    }

    class Max(override val size: Double) : Constraint {
        override fun compute(min: Double, max: Double) = max
    }
}