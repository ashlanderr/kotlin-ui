package com.github.ashlanderr.kotlin.ui.core

class EventProcessor(private val root: Node) {
    fun mouseDown(point: Point) = mouseEvent(point, EventsTarget::mouseDown)
    fun mouseUp(point: Point) = mouseEvent(point, EventsTarget::mouseUp)
    fun mouseClick(point: Point) = mouseEvent(point, EventsTarget::mouseClick)

    private fun mouseEvent(point: Point, handler: EventsTarget.(MouseEvent) -> Boolean) {
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
            if (current is EventsTarget) {
                val currentPoint = Point(point.x - current.renderLeft, point.y - current.renderTop)
                val event = MouseEvent(currentPoint, target)
                bubbling = current.handler(event)
            }
            current = current.parent
        }
    }
}