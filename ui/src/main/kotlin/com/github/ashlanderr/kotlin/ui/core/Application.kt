package com.github.ashlanderr.kotlin.ui.core

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JFrame
import javax.swing.Timer

@Suppress("LeakingThis")
abstract class Application : Node, JFrame() {
    final override val renderLeft: Double = 0.0
    final override val renderTop: Double = 0.0
    final override val renderWidth: Double = 0.0
    final override val renderHeight: Double = 0.0
    final override val parent: Node? = null
    final override val key: Any? = null

    private var bufferWidth = 0
    private var bufferHeight = 0
    private var bufferImage: Image? = null
    private var bufferGraphics: Graphics? = null

    private var root: Node = EmptyNode
    private var animationRequested = false
    private val animationTimer = Timer(1000 / 60) { animationFrame() }
    private val eventProcessor = EventProcessor(this)

    private val mouseListener = object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            val point = Point(e.point.x.toDouble(), e.point.y.toDouble())
            eventProcessor.mouseClick(point)
        }

        override fun mousePressed(e: MouseEvent) {
            val point = Point(e.point.x.toDouble(), e.point.y.toDouble())
            eventProcessor.mouseDown(point)
        }

        override fun mouseReleased(e: MouseEvent) {
            val point = Point(e.point.x.toDouble(), e.point.y.toDouble())
            eventProcessor.mouseUp(point)
        }
    }

    init {
        addMouseListener(mouseListener)
    }

    final override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {}
    final override fun arrange(left: Double, top: Double) {}
    final override fun render(g: Graphics2D) {}
    final override fun mount(parent: Node?) {}
    final override fun unmount() {}
    final override fun childAtPoint(point: Point) = root.childAtPoint(point)

    abstract fun render(): Node

    final override fun paint(g: Graphics) {
        val buffer = getBuffer()

        buffer.color = Color.WHITE
        buffer.clearRect(0, 0, width, height)
        buffer.setClip(0, 0, width, height)

        root = MergeProcessor.merge(root, render(), this)
        root.measure(buffer, Constraint.Max(bufferWidth.toDouble()), Constraint.Max(bufferHeight.toDouble()))
        root.arrange(0.0, 0.0)
        root.render(buffer)

        g.drawImage(bufferImage, 0, 0, this)
    }

    fun requestAnimationFrame() {
        animationRequested = true
        animationTimer.start()
    }

    private fun getBuffer(): Graphics2D {
        if (bufferImage == null || bufferWidth != width || bufferHeight != height) {
            bufferGraphics?.dispose()
            bufferImage?.flush()

            bufferImage = createImage(width, height)
            bufferGraphics = bufferImage!!.graphics

            bufferWidth = width
            bufferHeight = height
        }

        return bufferGraphics as Graphics2D
    }

    private fun animationFrame() {
        if (!animationRequested) animationTimer.stop()
        animationRequested = false
        repaint()
    }
}