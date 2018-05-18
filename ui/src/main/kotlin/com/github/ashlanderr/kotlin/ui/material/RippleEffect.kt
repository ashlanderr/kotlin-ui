package com.github.ashlanderr.kotlin.ui.material

import com.github.ashlanderr.kotlin.ui.core.*
import com.github.ashlanderr.kotlin.ui.graphics.AnimationCompletedHandler
import com.github.ashlanderr.kotlin.ui.graphics.AnimationMode
import com.github.ashlanderr.kotlin.ui.graphics.animation
import com.github.ashlanderr.kotlin.ui.graphics.canvas
import java.awt.Color

class RippleEffect : Component<RippleEffectState, RippleEffect>() {
    @ReactiveProperty
    var onCompleted: AnimationCompletedHandler? = null

    @ReactiveProperty
    var x = 0.0

    @ReactiveProperty
    var y = 0.0

    override fun initState() = RippleEffectState()
}

class RippleEffectState : State<RippleEffectState, RippleEffect>() {
    override fun render() = animation {
        duration = 0.5
        mode = AnimationMode.SINGLE
        onCompleted = component.onCompleted
        onFrame = { t ->
            canvas { g ->
                val radius = t * 100.0
                val left = component.x - radius
                val top = component.y - radius
                val width = radius * 2
                val height = radius * 2
                val alpha = (1 - t) * 0.8
                g.color = Color(211,47,47, (alpha * 255).toInt())
                g.fillOval(left.toInt(), top.toInt(), width.toInt(), height.toInt())
            }
        }
    }
}

fun rippleEffect(builder: Builder<RippleEffect>) = build(builder)