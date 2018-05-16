package com.github.ashlanderr.kotlin.ui.graphics

interface AnimationFunction {
    fun transform(x: Double): Double

    object LINEAR : AnimationFunction {
        override fun transform(x: Double) = x
    }
}