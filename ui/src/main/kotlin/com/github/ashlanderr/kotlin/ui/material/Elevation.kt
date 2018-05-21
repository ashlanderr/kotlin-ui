package com.github.ashlanderr.kotlin.ui.material

import com.github.ashlanderr.kotlin.ui.core.Node
import com.github.ashlanderr.kotlin.ui.core.ReactiveProperty
import com.github.ashlanderr.kotlin.ui.core.State
import com.github.ashlanderr.kotlin.ui.core.StatefulComponent
import com.github.ashlanderr.kotlin.ui.graphics.Canvas
import com.github.ashlanderr.kotlin.ui.layout.Indent
import com.github.ashlanderr.kotlin.ui.layout.Padding
import com.github.ashlanderr.kotlin.ui.layout.Stack
import java.awt.Color
import java.awt.GradientPaint
import java.awt.Graphics2D
import java.awt.RadialGradientPaint
import kotlin.math.abs
import kotlin.math.min

class Elevation(
    @ReactiveProperty var elevation: Double,
    @ReactiveProperty var child: Node,
    key: Any? = null
) : StatefulComponent<ElevationState, Elevation>(key) {
    override fun initState(component: Elevation) = ElevationState(component)
}

class ElevationState(override val component: Elevation) : State() {
    override fun render() = Stack(
        children = mutableListOf(
            ElevationCanvas(component.elevation),
            Padding(
                padding = Indent.all(component.elevation),
                child = component.child
            )
        )
    )
}

class ElevationCanvas(@ReactiveProperty var elevation: Double) : Canvas() {
    override fun render(g: Graphics2D, w: Double, h: Double) {
        if (elevation <= 0.0) return

        val outerColor = Color(0, 0, 0, 0)
        val innerColor = Color(0, 0, 0, 64)

        val outerLeft = 0.0
        val outerTop = 0.0
        val outerRight = w
        val outerBottom = h

        val innerLeft = elevation + 1.0
        val innerTop = elevation + 1.0
        val innerRight = w - elevation
        val innerBottom = h - elevation

        g.hGradient(outerColor, innerColor, outerLeft, innerTop, innerLeft, innerBottom)
        g.hGradient(innerColor, outerColor, innerRight, innerTop, outerRight, innerBottom)
        g.vGradient(outerColor, innerColor, innerLeft, outerTop, innerRight, innerTop)
        g.vGradient(innerColor, outerColor, innerLeft, innerBottom, innerRight, outerBottom)

        g.rGradient(outerColor, innerColor, outerLeft, outerTop, innerLeft, innerTop)
        g.rGradient(outerColor, innerColor, outerRight, outerTop, innerRight, innerTop)
        g.rGradient(outerColor, innerColor, outerLeft, outerBottom, innerLeft, innerBottom)
        g.rGradient(outerColor, innerColor, outerRight, outerBottom, innerRight, innerBottom)
    }

    private fun Graphics2D.hGradient(firstColor: Color, secondColor: Color, left: Double, top: Double, right: Double, bottom: Double) {
        paint = GradientPaint(left.toFloat(), 0.0f, firstColor, right.toFloat(), 0.0f, secondColor)
        fillRect(left.toInt(), top.toInt(), (right - left).toInt(), (bottom - top).toInt())
    }

    private fun Graphics2D.vGradient(firstColor: Color, secondColor: Color, left: Double, top: Double, right: Double, bottom: Double) {
        paint = GradientPaint(0.0f, top.toFloat(), firstColor, 0.0f, bottom.toFloat(), secondColor)
        fillRect(left.toInt(), top.toInt(), (right - left).toInt(), (bottom - top).toInt())
    }

    private fun Graphics2D.rGradient(firstColor: Color, secondColor: Color, left: Double, top: Double, right: Double, bottom: Double) {
        paint = RadialGradientPaint(
            right.toFloat(),
            bottom.toFloat(),
            abs(right - left).toFloat(),
            floatArrayOf(0.0f, 1.0f),
            arrayOf(secondColor, firstColor)
        )
        val x = min(left, right)
        val y = min(top, bottom)
        fillRect(x.toInt(), y.toInt(), abs(right - left).toInt(), abs(bottom - top).toInt())
    }
}