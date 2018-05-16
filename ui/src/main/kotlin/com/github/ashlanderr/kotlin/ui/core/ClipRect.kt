package com.github.ashlanderr.kotlin.ui.core

import java.awt.Graphics
import java.awt.Rectangle
import kotlin.math.max
import kotlin.math.min

fun Graphics.pushClip(x: Int, y: Int, width: Int, height: Int): Rectangle {
    val prev = this.clipBounds
    val prevRight = prev.x + prev.width
    val prevBottom = prev.y + prev.height

    val newLeft = max(prev.x, x)
    val newTop = max(prev.y, y)
    val newRight = min(prevRight, x + width)
    val newBottom = min(prevBottom, y + height)
    val newWidth = newRight - newLeft
    val newHeight = newBottom - newTop

    this.setClip(newLeft, newTop, newWidth, newHeight)

    return prev
}

fun Graphics.popClip(saved: Rectangle) {
    this.setClip(saved.x, saved.y, saved.width, saved.height)
}