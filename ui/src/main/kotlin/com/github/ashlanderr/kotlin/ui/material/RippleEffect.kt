package com.github.ashlanderr.kotlin.ui.material

import com.github.ashlanderr.kotlin.ui.core.*
import com.github.ashlanderr.kotlin.ui.graphics.AnimationController
import com.github.ashlanderr.kotlin.ui.graphics.AnimationManager
import com.github.ashlanderr.kotlin.ui.graphics.AnimationMode
import com.github.ashlanderr.kotlin.ui.graphics.canvas
import com.github.ashlanderr.kotlin.ui.layout.stack
import java.awt.Color
import kotlin.math.sqrt

class RippleEffect : Component<RippleEffectState, RippleEffect>() {
    @ReactiveProperty
    var child: Node = EmptyNode

    override fun initState() = RippleEffectState()
}

class RippleEffectState : State<RippleEffectState, RippleEffect>() {
    private inner class RippleState(val point: Point) {
        private val growAnimation = animations.add(AnimationController(
                mode = AnimationMode.SINGLE,
                duration = 0.5,
                running = true
        ))

        private val fadeAnimation = animations.add(AnimationController(
                mode = AnimationMode.SINGLE,
                duration = 0.5,
                running = false,
                onCompleted = {
                    ripples.remove(this)
                }
        ))

        fun released() {
            fadeAnimation.resume()
        }

        fun render() = canvas { g, w, h ->
            val radius = growAnimation.value * sqrt(w * w + h * h)
            val left = point.x - radius
            val top = point.y - radius
            val width = radius * 2
            val height = radius * 2
            val alpha = (1 - fadeAnimation.value) * 0.2
            g.color = Color(0, 0, 0, (alpha * 255).toInt())
            g.fillOval(left.toInt(), top.toInt(), width.toInt(), height.toInt())
        }
    }

    private val ripples = mutableListOf<RippleState>()
    private val animations = AnimationManager(this)

    override fun render(): Node {
        animations.update()
        return eventListener {
            onMouseDown = {
                update {
                    val ripple = RippleState(it.point)
                    ripples.add(ripple)
                }
                true
            }
            onMouseUp = {
                update {
                    ripples.forEach { it.released() }
                }
                true
            }
            child = stack {
                +component.child
                ripples.forEach { +it.render() }
            }
        }
    }
}

fun rippleEffect(builder: Builder<RippleEffect>) = build(builder)