package com.github.ashlanderr.kotlin.ui.material

import com.github.ashlanderr.kotlin.ui.core.*
import com.github.ashlanderr.kotlin.ui.graphics.Background
import com.github.ashlanderr.kotlin.ui.text.DefaultTextStyle

class FlatButton(
    var content: Node,
    var style: ButtonStyle = ButtonStyle.DEFAULT,
    var enabled: Boolean = true,
    var onClick: () -> Unit = {},
    key: Any? = null
) : StatefulComponent<FlatButtonState, FlatButton>(key) {

    override fun initState(component: FlatButton) = FlatButtonState(component)
}

class FlatButtonState(override val component: FlatButton) : State() {
    private var hover = false
    private var down = false

    override fun render(): Node {
        val theme = Theme.of(this)
        val style = component.style
        val textColor = if (component.enabled) style.text(theme) else theme.disabledColor
        val rippleColor = style.ripple(theme)

        val backgroundAlpha = if (hover && component.enabled) 16 else 0
        val backgroundColor = theme.textColor.copy(alpha = backgroundAlpha)

        return EventListener(
            onMouseEnter = this::onMouseEnter,
            onMouseLeave = this::onMouseLeave,
            onMouseUp = this::onMouseUp,
            onMouseDown = this::onMouseDown,
            child = Background(
                color = backgroundColor,
                child = RippleEffect(
                    color = rippleColor,
                    enabled = component.enabled,
                    child = DefaultTextStyle(
                        data = theme.textTheme.button.copy(
                            color = textColor
                        ),
                        child = component.content
                    )
                )
            )
        )
    }

    private fun onMouseUp(event: MouseEvent) = update {
        event.preventBubbling()
        if (down) {
            down = false
            component.onClick()
        }
    }

    private fun onMouseDown(event: MouseEvent) = update {
        event.preventBubbling()
        down = component.enabled
    }

    private fun onMouseEnter(event: MouseEvent) = update {
        hover = true
    }

    private fun onMouseLeave(event: MouseEvent) = update {
        hover = false
        down = false
    }
}