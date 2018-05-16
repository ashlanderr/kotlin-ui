package com.github.ashlanderr.core

import java.awt.Graphics

interface Node {
    val renderLeft: Double
    val renderTop: Double
    val renderWidth: Double
    val renderHeight: Double
    val parent: Node?

    fun measure(g: Graphics, maxWidth: Double, maxHeight: Double)
    fun arrange(left: Double, top: Double)
    fun render(g: Graphics)

    fun mount(parent: Node?)
    fun unmount()
}