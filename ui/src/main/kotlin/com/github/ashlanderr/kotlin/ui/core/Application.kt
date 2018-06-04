package com.github.ashlanderr.kotlin.ui.core

import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.AffineTransform
import javax.swing.JFrame
import javax.swing.Timer

@Suppress("LeakingThis")
abstract class Application : Node, JFrame() {
    final override val renderTransform = AffineTransform()
    final override val renderWidth: Double = 0.0
    final override val renderHeight: Double = 0.0
    final override val parent: Node? = null
    final override val key: Any? = null

    private var bufferWidth = 0
    private var bufferHeight = 0
    private var bufferImage: Image? = null
    private var bufferGraphics: Graphics2D? = null

    private var root: Node = EmptyNode
    private var animationRequested = false
    private val animationTimer = Timer(1000 / 60) { animationFrame() }
    private val eventProcessor = EventProcessor(this)

    private val mouseListener = object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) = mouseEvent(e, EventProcessor::mouseClick)
        override fun mousePressed(e: MouseEvent) = mouseEvent(e, EventProcessor::mouseDown)
        override fun mouseReleased(e: MouseEvent) = mouseEvent(e, EventProcessor::mouseUp)
        override fun mouseMoved(e: MouseEvent) = mouseEvent(e, EventProcessor::mouseMove)
        override fun mouseEntered(e: MouseEvent) = mouseEvent(e, EventProcessor::mouseEnter)
        override fun mouseExited(e: MouseEvent) = mouseEvent(e, EventProcessor::mouseLeave)
        override fun mouseDragged(e: MouseEvent) = mouseEvent(e, EventProcessor::mouseDrag)

        private fun mouseEvent(e: MouseEvent, handler: EventProcessor.(com.github.ashlanderr.kotlin.ui.core.MouseEvent) -> Unit) {
            val point = Point(e.point.x.toDouble(), e.point.y.toDouble())
            val screenPoint = Point(e.xOnScreen.toDouble(), e.yOnScreen.toDouble())
            val event = com.github.ashlanderr.kotlin.ui.core.MouseEvent(point, screenPoint, this@Application)
            eventProcessor.handler(event)
        }
    }

    init {
        addMouseListener(mouseListener)
        addMouseMotionListener(mouseListener)
    }

    final override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {}
    final override fun arrange(transform: AffineTransform) {}
    final override fun render(g: Graphics2D) {}
    final override fun mount(parent: Node?) {}
    final override fun unmount() {}
    override fun children() = listOf(root)
    override fun parentToLocal(point: Point) = point

    abstract fun render(): Node

    final override fun paint(g: Graphics) {
        val buffer = getBuffer()

        buffer.color = Color.WHITE
        buffer.clearRect(0, 0, width, height)
        buffer.setClip(0, 0, width, height)

        root = MergeProcessor.merge(root, render(), this)
        root.measure(buffer, Constraint.Max(bufferWidth.toDouble()), Constraint.Max(bufferHeight.toDouble()))
        root.arrange(AffineTransform())
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
            bufferGraphics = bufferImage!!.graphics as Graphics2D
            bufferGraphics!!.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)

            bufferWidth = width
            bufferHeight = height
        }

        return bufferGraphics!!
    }

    private fun animationFrame() {
        if (!animationRequested) animationTimer.stop()
        animationRequested = false
        repaint()
    }
}