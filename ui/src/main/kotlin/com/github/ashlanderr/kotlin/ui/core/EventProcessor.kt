package com.github.ashlanderr.kotlin.ui.core

class EventProcessor(private val root: Node) {
    private var mouseStack: List<Node> = emptyList()

    fun mouseDown(point: Point) = mouseEvent(point, EventsTarget::mouseDown)
    fun mouseUp(point: Point) = mouseEvent(point, EventsTarget::mouseUp)
    fun mouseMove(point: Point) = mouseEvent(point, EventsTarget::mouseMove)
    fun mouseClick(point: Point) = mouseEvent(point, EventsTarget::mouseClick)
    fun mouseEnter(point: Point) = mouseEvent(point, { })
    fun mouseLeave(point: Point) = updateMouseStack(point, emptyList())

    private fun mouseEvent(point: Point, handler: EventsTarget.(MouseEvent) -> Unit) {
        val stack = mutableListOf<Node>()

        var current: Node? = root
        var target = root
        var next: Node? = current

        while (next != null) {
            current = next
            target = next
            stack.add(next)
            next = current.childAtPoint(point)
        }

        updateMouseStack(point, stack)

        var bubbling = true

        while (bubbling && current != null) {
            bubbling = sendEvent(current, target, point, handler)
            current = current.parent
        }
    }

    private fun updateMouseStack(point: Point, newStack: List<Node>) {
        val commonSize = mouseStack
            .zip(newStack)
            .takeWhile { it.first == it.second }
            .size

        for (i in mouseStack.size - 1 downTo commonSize) {
            val node = mouseStack[i]
            sendEvent(node, node, point, EventsTarget::mouseLeave)
        }

        for (i in commonSize until newStack.size) {
            val node = newStack[i]
            sendEvent(node, node, point, EventsTarget::mouseEnter)
        }

        mouseStack = newStack
    }

    private fun sendEvent(node: Node, target: Node, point: Point, handler: EventsTarget.(MouseEvent) -> Unit): Boolean {
        return if (node is EventsTarget) {
            val currentPoint = Point(point.x - node.renderLeft, point.y - node.renderTop)
            val event = MouseEvent(currentPoint, target)
            node.handler(event)
            event.bubbling
        } else {
            true
        }
    }
}