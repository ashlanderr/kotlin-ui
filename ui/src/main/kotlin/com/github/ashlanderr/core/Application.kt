package com.github.ashlanderr.core

import java.awt.Color
import java.awt.Graphics
import java.awt.Image
import javax.swing.JFrame
import javax.swing.Timer

abstract class Application : Node, JFrame() {
    final override val renderLeft: Double = 0.0
    final override val renderTop: Double = 0.0
    final override val renderWidth: Double = 0.0
    final override val renderHeight: Double = 0.0
    final override val parent: Node? = null

    private var bufferWidth = 0
    private var bufferHeight = 0
    private var bufferImage: Image? = null
    private var bufferGraphics: Graphics? = null

    private var root: Node = EmptyNode
    private var animationRequested = false
    private val animationTimer = Timer(1000 / 60) { animationFrame() }

    final override fun measure(g: Graphics, maxWidth: Double, maxHeight: Double) { }
    final override fun arrange(left: Double, top: Double) { }
    final override fun render(g: Graphics) { }
    final override fun mount(parent: Node?) { }
    final override fun unmount() { }

    abstract fun render(): Node

    final override fun paint(g: Graphics) {
        Timer(100, {}).start()

        val buffer = getBuffer()

        buffer.color = Color.WHITE
        buffer.clearRect(0, 0, width, height)
        buffer.setClip(0, 0, width, height)

        root = MergeProcessor.merge(root, render(), this)
        root.measure(buffer, bufferWidth.toDouble(), bufferHeight.toDouble())
        root.arrange(0.0, 0.0)
        root.render(buffer)

        g.drawImage(bufferImage, 0, 0, this)
    }

    fun requestAnimationFrame() {
        animationRequested = true
        animationTimer.start()
    }

    private fun getBuffer(): Graphics {
        if (bufferImage == null || bufferWidth != width || bufferHeight != height) {
            bufferGraphics?.dispose()
            bufferImage?.flush()

            bufferImage = createImage(width, height)
            bufferGraphics = bufferImage!!.graphics

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