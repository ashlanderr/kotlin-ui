package com.github.ashlanderr.kotlin.ui.core

import java.awt.Graphics2D
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

abstract class AbstractEvent(val target: Node) {
    @Suppress("UNCHECKED_CAST")
    override fun toString(): String {
        val clazz = this::class
        val props = clazz.memberProperties
            .map { Pair(it.name, (it as KProperty1<AbstractEvent, Any?>).get(this)) }
        return clazz.simpleName + props.joinToString(", ", "(", ")") { "${it.first}=${it.second}" }
    }
}

class MouseEvent(val point: Point, target: Node) : AbstractEvent(target)

interface EventsTarget {
    fun mouseDown(event: MouseEvent): Boolean
    fun mouseUp(event: MouseEvent): Boolean
    fun mouseClick(event: MouseEvent): Boolean
    fun mouseLeave(event: MouseEvent): Boolean
    fun mouseEnter(event: MouseEvent): Boolean
    fun mouseMove(event: MouseEvent): Boolean
}

class EventListener(
    var onMouseDown: (MouseEvent) -> Boolean = { true },
    var onMouseUp: (MouseEvent) -> Boolean = { true },
    var onMouseClick: (MouseEvent) -> Boolean = { true },
    var onMouseLeave: (MouseEvent) -> Boolean = { true },
    var onMouseEnter: (MouseEvent) -> Boolean = { true },
    var onMouseMove: (MouseEvent) -> Boolean = { true },
    @RxNode var child: Node,
    key: Any? = null
) : EventsTarget, AbstractNode(key) {

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        child.measure(g, w, h)
        renderWidth = child.renderWidth
        renderHeight = child.renderHeight
    }

    override fun arrange(left: Double, top: Double) {
        child.arrange(left, top)
        renderLeft = left
        renderTop = top
    }

    override fun render(g: Graphics2D) {
        child.render(g)
    }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }

    override fun childAtPoint(point: Point) = child.takeIf { it.containsPoint(point) }

    override fun mouseDown(event: MouseEvent) = onMouseDown(event)
    override fun mouseUp(event: MouseEvent) = onMouseUp(event)
    override fun mouseClick(event: MouseEvent) = onMouseClick(event)
    override fun mouseLeave(event: MouseEvent) = onMouseLeave(event)
    override fun mouseEnter(event: MouseEvent) = onMouseEnter(event)
    override fun mouseMove(event: MouseEvent) = onMouseMove(event)
}