package com.github.ashlanderr.kotlin.ui.core

import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.geom.Point2D

fun AffineTransform.transform(point: Point): Point {
    val src = Point2D.Double(point.x, point.y)
    val dest = Point2D.Double()
    this.transform(src, dest)
    return Point(dest.x, dest.y)
}

fun Graphics2D.withTransform(transform: AffineTransform, block: () -> Unit) {
    val saved = this.transform
    this.transform(transform)
    block()
    this.transform = saved
}