package com.github.ashlanderr.kotlin.ui.graphics

import com.github.ashlanderr.kotlin.ui.core.State

typealias AnimationCompletedHandler = () -> Unit

class AnimationController(
    private val function: AnimationFunction = AnimationFunction.LINEAR,
    private val mode: AnimationMode = AnimationMode.INFINITE,
    private val duration: Double = 1.0,
    private var onCompleted: AnimationCompletedHandler? = null,
    running: Boolean = true
) : Updatable {
    enum class AnimationState {
        RUNNING,
        PAUSED,
        COMPLETED
    }

    private var runningState = if (running) AnimationState.RUNNING else AnimationState.PAUSED
    private var lastTimestamp = System.nanoTime()
    private var times = 0
    private var x = 0.0

    var value: Double = 0.0
        private set

    fun resume() {
        if (runningState == AnimationState.PAUSED) {
            runningState = AnimationState.RUNNING
            lastTimestamp = System.nanoTime()
        }
    }

    fun pause() {
        if (runningState == AnimationState.RUNNING) {
            runningState = AnimationState.PAUSED
        }
    }

    fun restart() {
        runningState = AnimationState.RUNNING
        lastTimestamp = System.nanoTime()
        times = 0
        x = 0.0
        value = 0.0
    }

    override fun update(state: State): Boolean {
        if (runningState == AnimationState.RUNNING) {
            state.update {
                val newTimestamp = System.nanoTime()
                x += (newTimestamp - lastTimestamp) / (1000000000.0 * duration)
                lastTimestamp = newTimestamp

                if (x > 1.0) {
                    times += 1
                    if (mode.isCompleted(times)) {
                        runningState = AnimationState.COMPLETED
                        x = 1.0
                        onCompleted?.invoke()
                    } else {
                        x = 0.0
                    }
                }
            }
        }

        value = function.transform(x)
        return runningState == AnimationState.COMPLETED
    }
}