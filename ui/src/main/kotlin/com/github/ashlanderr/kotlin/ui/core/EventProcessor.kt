package com.github.ashlanderr.kotlin.ui.core

class EventProcessor(private val root: Node) {
    private var mouseStack: List<Node> = emptyList()

    fun mouseDown(event: MouseEvent) = mouseEvent(event, EventsTarget::mouseDown)
    fun mouseUp(event: MouseEvent) = mouseEvent(event, EventsTarget::mouseUp)
    fun mouseMove(event: MouseEvent) = mouseEvent(event, EventsTarget::mouseMove)
    fun mouseClick(event: MouseEvent) = mouseEvent(event, EventsTarget::mouseClick)
    fun mouseEnter(event: MouseEvent) = mouseEvent(event, { })
    fun mouseLeave(event: MouseEvent) = updateMouseStack(event, emptyList())
    fun mouseDrag(event: MouseEvent) = mouseEvent(event, EventsTarget::mouseDrag)

    private fun mouseEvent(event: MouseEvent, handler: EventsTarget.(MouseEvent) -> Unit) {
        val stack = mutableListOf<Node>()

        var current: Node? = root
        var target = root
        var next: Node? = current

        while (next != null) {
            current = next
            target = next
            stack.add(next)
            next = current.childAtPoint(event.point)
        }

        updateMouseStack(event, stack)

        var bubbling = true

        while (bubbling && current != null) {
            bubbling = sendEvent(current, target, event, handler)
            current = current.parent
        }
    }

    private fun updateMouseStack(event: MouseEvent, newStack: List<Node>) {
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

    private fun sendEvent(node: Node, target: Node, event: MouseEvent, handler: EventsTarget.(MouseEvent) -> Unit): Boolean {
        return if (node is EventsTarget) {
            val currentPoint = Point(event.point.x - node.renderLeft, event.point.y - node.renderTop)
            val currentEvent = MouseEvent(currentPoint, event.screenPoint, target)
            node.handler(currentEvent)
            currentEvent.bubbling
        } else {
            true
        }
    }
}