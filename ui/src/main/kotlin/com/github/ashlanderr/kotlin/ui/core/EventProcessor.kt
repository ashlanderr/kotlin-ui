package com.github.ashlanderr.kotlin.ui.core

class EventProcessor(private val root: Node) {
    fun mouseDown(point: Point) = mouseEvent(point, EventListener::mouseDown)
    fun mouseUp(point: Point) = mouseEvent(point, EventListener::mouseUp)
    fun mouseClick(point: Point) = mouseEvent(point, EventListener::mouseClick)

    private fun mouseEvent(point: Point, handler: EventListener.(MouseEvent) -> Boolean) {
        var current: Node? = root
        var target = root
        var next: Node? = current

        while (next != null) {
            current = next
            target = next
            next = current.childAtPoint(point)
        }

        var bubbling = true

        while (bubbling && current != null) {
            if (current is EventListener) {
                val currentPoint = Point(point.x - current.renderLeft, point.y - current.renderTop)
                val event = MouseEvent(currentPoint, target)
                bubbling = current.handler(event)
            }
            current = current.parent
        }
    }
}