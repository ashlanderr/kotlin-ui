package com.github.ashlanderr.kotlin.ui.graphics

import com.github.ashlanderr.kotlin.ui.core.State

typealias AnimationCompletedHandler = () -> Unit

class AnimationManager(private val state: State) {
    private val animations = mutableListOf<AnimationController>()

    fun add(animation: AnimationController): AnimationController {
        animations.add(animation)
        return animation
    }

    fun remove(animation: AnimationController) {
        animations.remove(animation)
    }

    fun update() {
        animations.removeIf { it.update(state) }
    }
}

class AnimationController(
    private val function: AnimationFunction = AnimationFunction.LINEAR,
    private val mode: AnimationMode = AnimationMode.INFINITE,
    private val duration: Double = 1.0,
    private var onCompleted: AnimationCompletedHandler? = null,
    running: Boolean = true
) {
    enum class AnimationState {
        RUNNING,
        PAUSED,
        COMPLETED
    }

    private var state = if (running) AnimationState.RUNNING else AnimationState.PAUSED
    private var lastTimestamp = System.nanoTime()
    private var times = 0
    private var x = 0.0

    var value: Double = 0.0
        private set

    fun resume() {
        if (state == AnimationState.PAUSED) {
            state = AnimationState.RUNNING
            lastTimestamp = System.nanoTime()
        }
    }

    fun pause() {
        if (state == AnimationState.RUNNING) {
            state = AnimationState.PAUSED
        }
    }

    fun update(componentState: State): Boolean {
        if (state == AnimationState.RUNNING) {
            componentState.update {
                val newTimestamp = System.nanoTime()
                x += (newTimestamp - lastTimestamp) / (1000000000.0 * duration)
                lastTimestamp = newTimestamp

                if (x > 1.0) {
                    times += 1
                    if (mode.isCompleted(times)) {
                        state = AnimationState.COMPLETED
                        x = 1.0
                        onCompleted?.invoke()
                    } else {
                        x = 0.0
                    }
                }
            }
        }

        value = function.transform(x)
        return state == AnimationState.COMPLETED
    }
}