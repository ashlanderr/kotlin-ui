package com.github.ashlanderr.kotlin.ui.graphics

import com.github.ashlanderr.kotlin.ui.core.State

abstract class TransitionController<T>(
    initialValue: T,
    function: AnimationFunction = AnimationFunction.LINEAR,
    duration: Double = 1.0
) : Updatable {

    private var oldValue = initialValue
    private var newValue = initialValue
    private var animation = AnimationController(function, AnimationMode.SINGLE, duration, running = false)

    var value: T = initialValue
        private set

    fun transition(newValue: T) {
        this.oldValue = value
        this.newValue = newValue
        animation.restart()
    }

    protected abstract fun interpolate(oldValue: T, newValue: T, time: Double): T

    override fun update(state: State): Boolean {
        animation.update(state)
        value = interpolate(oldValue, newValue, animation.value)
        return false
    }
}

class DoubleTransition(
    initialValue: Double = 0.0,
    function: AnimationFunction = AnimationFunction.LINEAR,
    duration: Double = 1.0
) : TransitionController<Double>(initialValue, function, duration) {
    override fun interpolate(oldValue: Double, newValue: Double, time: Double) = oldValue * (1 - time) + newValue * time
}