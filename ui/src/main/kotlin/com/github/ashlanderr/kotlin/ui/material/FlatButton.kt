package com.github.ashlanderr.kotlin.ui.material

import com.github.ashlanderr.kotlin.ui.core.*
import com.github.ashlanderr.kotlin.ui.graphics.AnimationManager
import com.github.ashlanderr.kotlin.ui.graphics.Background
import com.github.ashlanderr.kotlin.ui.graphics.DoubleTransition
import com.github.ashlanderr.kotlin.ui.graphics.WithCursor
import com.github.ashlanderr.kotlin.ui.text.DefaultTextStyle
import java.awt.Cursor

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
    private val animations = AnimationManager(this)
    private val hoverTransition = animations.add(DoubleTransition(
        initialValue = 0.0,
        duration = 0.2
    ))

    private var down = false

    override fun render(): Node {
        animations.update()

        val theme = Theme.of(this)
        val style = component.style
        val textColor = if (component.enabled) style.text(theme) else theme.disabledColor
        val rippleColor = style.ripple(theme)

        val backgroundColor = theme.textColor.copy(alpha = (hoverTransition.value * 255).toInt())

        return EventListener(
            onMouseEnter = this::onMouseEnter,
            onMouseLeave = this::onMouseLeave,
            onMouseUp = this::onMouseUp,
            onMouseDown = this::onMouseDown,
            child = WithCursor(
                cursor = Cursor(Cursor.HAND_CURSOR),
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
        if (component.enabled) {
            hoverTransition.transition(0.125)
        }
    }

    private fun onMouseLeave(event: MouseEvent) = update {
        hoverTransition.transition(0.0)
        down = false
    }
}