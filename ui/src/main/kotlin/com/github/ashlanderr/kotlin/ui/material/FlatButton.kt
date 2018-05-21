package com.github.ashlanderr.kotlin.ui.material

import com.github.ashlanderr.kotlin.ui.core.EventListener
import com.github.ashlanderr.kotlin.ui.core.Node
import com.github.ashlanderr.kotlin.ui.core.State
import com.github.ashlanderr.kotlin.ui.core.StatefulComponent
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
    override fun render(): Node {
        val theme = Theme.of(this)
        val style = component.style
        val textColor = if (component.enabled) style.text(theme) else theme.disabledColor
        val rippleColor = style.ripple(theme)

        return EventListener(
            onMouseClick = {
                if (component.enabled) component.onClick()
                false
            },
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
    }
}