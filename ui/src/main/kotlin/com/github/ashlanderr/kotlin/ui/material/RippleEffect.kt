package com.github.ashlanderr.kotlin.ui.material

import com.github.ashlanderr.kotlin.ui.core.*
import com.github.ashlanderr.kotlin.ui.graphics.AnimationController
import com.github.ashlanderr.kotlin.ui.graphics.AnimationManager
import com.github.ashlanderr.kotlin.ui.graphics.AnimationMode
import com.github.ashlanderr.kotlin.ui.graphics.Canvas
import com.github.ashlanderr.kotlin.ui.layout.Stack
import java.awt.Color
import java.awt.Graphics2D
import kotlin.math.sqrt

class RippleEffect(
    @ReactiveProperty var child: Node,
    @ReactiveProperty var color: Color,
    @ReactiveProperty var enabled: Boolean = true,
    key: Any? = null
) : StatefulComponent<RippleEffectState, RippleEffect>(key) {

    override fun initState(component: RippleEffect) = RippleEffectState(component)
}

class RippleEffectState(override val component: RippleEffect) : State() {
    private class RippleKey(val point: Point, var released: Boolean)

    private val ripples = mutableListOf<RippleKey>()
    private val animations = AnimationManager(this)

    override fun render(): Node {
        animations.update()

        return EventListener(
            onMouseDown = {
                update {
                    if (component.enabled) ripples.add(RippleKey(it.point, false))
                }
                true
            },
            onMouseUp = {
                update {
                    ripples.forEach { it.released = true }
                }
                true
            },
            child = Stack(
                children = children(
                    listOf(component.child),
                    ripples.map {
                        Ripple(
                            key = it,
                            color = component.color,
                            point = it.point,
                            released = it.released,
                            onCompleted = { ripples.remove(it) }
                        )
                    }
                )
            )
        )
    }
}

class Ripple(
    key: Any? = null,
    @ReactiveProperty var color: Color,
    @ReactiveProperty var point: Point,
    @ReactiveProperty var released: Boolean,
    @ReactiveProperty var onCompleted: () -> Unit
) : StatefulComponent<RippleState, Ripple>(key) {
    override fun initState(component: Ripple) = RippleState(component)
}

class RippleState(override val component: Ripple) : State() {
    private val animations = AnimationManager(this)

    private val growAnimation = animations.add(AnimationController(
        mode = AnimationMode.SINGLE,
        duration = 0.5,
        running = true
    ))

    private val fadeAnimation = animations.add(AnimationController(
        mode = AnimationMode.SINGLE,
        duration = 0.5,
        running = false,
        onCompleted = component.onCompleted
    ))

    override fun render(): Node {
        animations.update()
        if (component.released) fadeAnimation.resume()
        return RippleCanvas(component.color, component.point, growAnimation.value, fadeAnimation.value)
    }
}

class RippleCanvas(
    @ReactiveProperty var color: Color,
    @ReactiveProperty var point: Point,
    @ReactiveProperty var radiusTime: Double,
    @ReactiveProperty var alphaTime: Double
) : Canvas() {
    override fun render(g: Graphics2D, w: Double, h: Double) {
        val radius = radiusTime * sqrt(w * w + h * h)
        val left = point.x - radius
        val top = point.y - radius
        val width = radius * 2
        val height = radius * 2
        val alpha = (1 - alphaTime) * 0.2
        g.color = Color(color.red, color.green, color.blue, (alpha * 255).toInt())
        g.fillOval(left.toInt(), top.toInt(), width.toInt(), height.toInt())
    }
}