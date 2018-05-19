package com.github.ashlanderr.kotlin.ui.core

import java.awt.Graphics
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

interface EventListener {
    fun mouseDown(event: MouseEvent): Boolean
    fun mouseUp(event: MouseEvent): Boolean
    fun mouseClick(event: MouseEvent): Boolean
}

class EventListenerNode : EventListener, AbstractNode() {
    @ReactiveProperty
    var onMouseDown: (MouseEvent) -> Boolean = { true }

    @ReactiveProperty
    var onMouseUp: (MouseEvent) -> Boolean = { true }

    @ReactiveProperty
    var onMouseClick: (MouseEvent) -> Boolean = { true }

    @ReactiveNode
    var child: Node = EmptyNode

    override fun measure(g: Graphics, w: Constraint, h: Constraint) {
        child.measure(g, w, h)
        renderWidth = child.renderWidth
        renderHeight = child.renderHeight
    }

    override fun arrange(left: Double, top: Double) {
        child.arrange(left, top)
        renderLeft = left
        renderTop = top
    }

    override fun render(g: Graphics) {
        child.render(g)
    }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }

    override fun childAtPoint(point: Point): Node? {
        return child.childAtPoint(point)
    }

    override fun mouseDown(event: MouseEvent) = onMouseDown(event)
    override fun mouseUp(event: MouseEvent) = onMouseUp(event)
    override fun mouseClick(event: MouseEvent) = onMouseClick(event)
}

fun eventListener(builder: Builder<EventListenerNode>) = build(builder)