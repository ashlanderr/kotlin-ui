package com.github.ashlanderr.kotlin.ui.core

import java.awt.Graphics

interface Node {
    val renderLeft: Double
    val renderTop: Double
    val renderWidth: Double
    val renderHeight: Double
    val parent: Node?

    fun measure(g: Graphics, w: Constraint, h: Constraint)
    fun arrange(left: Double, top: Double)
    fun render(g: Graphics)

    fun mount(parent: Node?)
    fun unmount()
}