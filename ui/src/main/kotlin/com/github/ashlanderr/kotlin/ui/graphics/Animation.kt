package com.github.ashlanderr.kotlin.ui.graphics

import com.github.ashlanderr.kotlin.ui.core.*

typealias AnimationFrameHandler = (time: Double) -> Node
typealias AnimationCompletedHandler = () -> Unit

class Animation : Component<AnimationState, Animation>() {
    var function: AnimationFunction = AnimationFunction.LINEAR
    var mode: AnimationMode = AnimationMode.INFINITE
    var duration: Double = 1.0
    var onFrame: AnimationFrameHandler? = null
    var onCompleted: AnimationCompletedHandler? = null

    override fun initState() = AnimationState()
}

class AnimationState : State<AnimationState, Animation>() {
    private var startTime = System.nanoTime()
    private var times = 0
    private var completed = false

    override fun render(): Node {
        var time = 1.0

        if (!completed) {
            update {
                val currentTime = System.nanoTime()
                time = (currentTime - startTime) / (1000000000.0 * component.duration)

                if (time > 1.0) {
                    times += 1
                    completed = component.mode.isCompleted(times)
                    if (!completed) {
                        time = 0.0
                        startTime = currentTime
                    } else {
                        time = 1.0
                        component.onCompleted?.invoke()
                    }
                }
            }
        }

        val x = component.function.transform(time)
        return component.onFrame?.invoke(x) ?: EmptyNode
    }
}

fun animation(builder: Builder<Animation>) = build(builder)