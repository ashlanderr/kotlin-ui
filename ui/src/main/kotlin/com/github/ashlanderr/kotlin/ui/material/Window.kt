package com.github.ashlanderr.kotlin.ui.material

import com.github.ashlanderr.kotlin.ui.core.*
import com.github.ashlanderr.kotlin.ui.graphics.Rect
import com.github.ashlanderr.kotlin.ui.graphics.WithCursor
import com.github.ashlanderr.kotlin.ui.layout.*
import com.github.ashlanderr.kotlin.ui.text.*
import java.awt.Cursor
import java.awt.Font
import java.awt.event.WindowEvent
import javax.swing.JFrame

class WindowButton(var icon: IconData, var action: (JFrame) -> Unit) : StatelessComponent() {
    override fun render(): Node {
        val theme = Theme.of(this)
        return FlatButton(
            onClick = this::onClick,
            content = Icon(
                icon = icon,
                size = 32.0,
                color = theme.alternateTextColor
            )
        )
    }

    private fun onClick() {
        val frame = ancestor<Application>()!!
        action(frame)
    }
}

class WindowTitle(var content: Node) : StatefulComponent<WindowTitleState, WindowTitle>() {
    override fun initState(component: WindowTitle) = WindowTitleState(component)
}

class WindowTitleState(override val component: WindowTitle) : State() {
    private var dragging = false
    private var firstPoint = Point(0.0, 0.0)
    private var windowX: Int = 0
    private var windowY: Int = 0

    override fun render(): Node {
        val theme = Theme.of(this)
        val cursor = if (dragging) Cursor.MOVE_CURSOR else Cursor.DEFAULT_CURSOR

        return EventListener(
            onMouseDrag = this::onMouseDrag,
            onMouseUp = this::onMouseUp,
            child = WithCursor(
                cursor = Cursor(cursor),
                child = DefaultTextStyle(
                    data = TextStyle(
                        color = theme.alternateTextColor,
                        fontSize = 16,
                        fontStyle = Font.BOLD
                    ),
                    child = Padding(
                        padding = Indent(8.0, 0.0, 0.0, 0.0),
                        child = component.content
                    )
                )
            )
        )
    }

    private fun onMouseDrag(event: MouseEvent) = update {
        if (!dragging) {
            val app = ancestorNode<Application>()!!
            val location = app.location
            firstPoint = event.screenPoint
            windowX = location.x
            windowY = location.y
            dragging = true
        }

        if (dragging) {
            val app = ancestorNode<Application>()!!
            app.setLocation(windowX + (event.screenPoint.x - firstPoint.x).toInt(), windowY + (event.screenPoint.y - firstPoint.y).toInt())
        }
    }

    private fun onMouseUp(event: MouseEvent) = update {
        dragging = false
    }
}

class Window(
    var title: Node,
    var content: Node = EmptyNode
) : StatefulComponent<WindowState, Window>() {
    override fun initState(component: Window) = WindowState(component)
}

class WindowState(override val component: Window) : State() {
    private var maximized = false

    override fun render(): Node {
        val theme = Theme.of(this)

        return Grid(
            columns = elements {
                flex(1.0)
            },
            rows = elements {
                auto()
                flex(1.0)
            },
            children = mutableListOf(
                windowBorderCell(theme),
                barCell(theme),
                contentCell()
            )
        )
    }

    private fun minimize(frame: JFrame) {
        frame.extendedState = frame.extendedState or JFrame.ICONIFIED
    }

    private fun maximize(frame: JFrame) = update {
        maximized = !maximized
        frame.extendedState = frame.extendedState xor JFrame.MAXIMIZED_BOTH
    }

    private fun close(frame: JFrame) {
        frame.dispatchEvent(WindowEvent(frame, WindowEvent.WINDOW_CLOSING))
    }

    private fun barCell(theme: ThemeData): Cell {
        val expandIcon = if (maximized) Icons.COMPRESS else Icons.EXPAND

        return Cell(
            column = 0,
            row = 0,
            child = Grid(
                columns = elements {
                    flex(1.0)
                    auto()
                    auto()
                    auto()
                },
                rows = elements {
                    auto()
                },
                children = mutableListOf(
                    barBackgroundCell(theme),
                    titleCell(),
                    buttonCell(1, Icons.MINUS, this::minimize),
                    buttonCell(2, expandIcon, this::maximize),
                    buttonCell(3, Icons.TIMES, this::close)
                )
            )
        )
    }

    private fun barBackgroundCell(theme: ThemeData): Cell {
        return Cell(
            column = 0,
            row = 0,
            columnSpan = 4,
            rowSpan = 1,
            child = Rect(
                stroke = null,
                fill = theme.primary1Color
            )
        )
    }

    private fun windowBorderCell(theme: ThemeData): Cell {
        return Cell(
            column = 0,
            row = 0,
            columnSpan = 1,
            rowSpan = 2,
            child = Rect(
                stroke = theme.primary1Color,
                fill = null
            )
        )
    }

    private fun titleCell(): Cell {
        return Cell(
            column = 0,
            row = 0,
            verticalAlign = VerticalAlign.CENTER,
            child = WindowTitle(
                content = component.title
            )
        )
    }

    private fun buttonCell(column: Int, icon: IconData, action: (JFrame) -> Unit): Cell {
        return Cell(
            column = column,
            row = 0,
            verticalAlign = VerticalAlign.CENTER,
            child = WindowButton(icon, action)
        )
    }

    private fun contentCell(): Cell {
        return Cell(
            column = 0,
            row = 1,
            child = Padding(
                padding = Indent(1.0, 0.0, 1.0, 1.0),
                child = component.content
            )
        )
    }
}