package com.github.ashlanderr.kotlin.ui.material

import java.awt.Color

interface ButtonStyle {
    fun text(theme: ThemeData): Color
    fun ripple(theme: ThemeData): Color

    object DEFAULT : ButtonStyle {
        override fun text(theme: ThemeData) = theme.textColor
        override fun ripple(theme: ThemeData) = theme.textColor
    }

    object PRIMARY : ButtonStyle {
        override fun text(theme: ThemeData) = theme.primary1Color
        override fun ripple(theme: ThemeData) = theme.textColor
    }

    object ACCENT : ButtonStyle {
        override fun text(theme: ThemeData) = theme.accent1Color
        override fun ripple(theme: ThemeData) = theme.textColor
    }
}