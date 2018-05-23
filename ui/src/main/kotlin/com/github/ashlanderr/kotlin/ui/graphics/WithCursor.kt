package com.github.ashlanderr.kotlin.ui.graphics

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Cursor

class WithCursor(
    var child: Node,
    var cursor: Cursor,
    key: Any? = null
) : StatefulComponent<WithCursorState, WithCursor>(key) {
    override fun initState(component: WithCursor) = WithCursorState(this)
}

class WithCursorState(override val component: WithCursor) : State() {
    lateinit var parentCursor: Cursor

    override fun render() = EventListener(
        onMouseEnter = this::onMouseEnter,
        onMouseLeave = this::onMouseLeave,
        child = component.child
    )

    private fun onMouseEnter(event: MouseEvent) {
        val app = ancestorNode<Application>()!!
        parentCursor = app.cursor
        app.cursor = component.cursor
    }

    private fun onMouseLeave(event: MouseEvent) {
        val app = ancestorNode<Application>()!!
        app.cursor = parentCursor
    }
}