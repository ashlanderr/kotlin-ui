package com.github.ashlanderr.kotlin.ui.core

import java.awt.geom.AffineTransform

class EventProcessor(private val root: Node) {
    private var mouseCaptured = false
    private var mouseStack: List<StackItem> = emptyList()

    private data class StackItem(val node: Node, val transform: AffineTransform)

    fun mouseDown(event: MouseEvent) = mouseEvent(event, EventsTarget::mouseDown, capture = true)
    fun mouseUp(event: MouseEvent) = mouseEvent(event, EventsTarget::mouseUp, release = true)
    fun mouseMove(event: MouseEvent) = mouseEvent(event, EventsTarget::mouseMove)
    fun mouseClick(event: MouseEvent) = mouseEvent(event, EventsTarget::mouseClick)
    fun mouseEnter(event: MouseEvent) = updateMouseStack(event, computeMouseStack(event))
    fun mouseLeave(event: MouseEvent) = updateMouseStack(event, emptyList())
    fun mouseDrag(event: MouseEvent) = mouseEvent(event, EventsTarget::mouseDrag)

    private fun mouseEvent(
        event: MouseEvent,
        handler: EventsTarget.(MouseEvent) -> Unit,
        capture: Boolean = false,
        release: Boolean = false
    ) {
        mouseCaptured = mouseCaptured and (!release)

        val stack = computeMouseStack(event)
        updateMouseStack(event, stack)
        bubbleEvent(event, handler)

        mouseCaptured = mouseCaptured or capture
    }

    private fun bubbleEvent(event: MouseEvent, handler: EventsTarget.(MouseEvent) -> Unit) {
        val target = mouseStack.last()
        val iterator = mouseStack.asReversed().iterator()
        var bubbling = true

        while (bubbling && iterator.hasNext()) {
            val current = iterator.next()
            bubbling = sendEvent(current, target, event, handler)
        }
    }

    private fun computeMouseStack(event: MouseEvent): List<StackItem> {
        if (mouseCaptured) return mouseStack

        val stack = mutableListOf<StackItem>()
        val transform = AffineTransform()

        var current: Node? = root
        var next: Node? = current
        var point = event.point

        while (next != null) {
            current = next
            val currentTransform = current.renderTransform.createInverse()
            transform.concatenate(currentTransform)
            stack.add(StackItem(current, AffineTransform(transform)))
            point = currentTransform.transform(point)
            next = current.children().childAtPoint(point)
        }

        return stack
    }

    private fun updateMouseStack(event: MouseEvent, newStack: List<StackItem>) {
        if (mouseCaptured) return

        val commonSize = mouseStack
            .zip(newStack)
            .takeWhile { it.first == it.second }
            .size

        for (i in mouseStack.size - 1 downTo commonSize) {
            val node = mouseStack[i]
            sendEvent(node, node, event, EventsTarget::mouseLeave)
        }

        for (i in commonSize until newStack.size) {
            val node = newStack[i]
            sendEvent(node, node, event, EventsTarget::mouseEnter)
        }

        mouseStack = newStack
    }

    private fun sendEvent(
        current: StackItem,
        target: StackItem,
        event: MouseEvent,
        handler: EventsTarget.(MouseEvent) -> Unit
    ): Boolean {
        val node = current.node
        return if (node is EventsTarget) {
            val currentPoint = current.transform.transform(event.point)
            val currentEvent = MouseEvent(currentPoint, event.screenPoint, target.node)
            node.handler(currentEvent)
            currentEvent.bubbling
        } else {
            true
        }
    }
}