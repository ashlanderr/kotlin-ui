package com.github.ashlanderr.kotlin.ui.material

import java.awt.Color

interface ButtonStyle {
    fun text(theme: ThemeData): Color
    fun ripple(theme: ThemeData): Color
    fun background(theme: ThemeData): Color

    object DEFAULT : ButtonStyle {
        override fun text(theme: ThemeData) = theme.textColor
        override fun ripple(theme: ThemeData) = theme.textColor
        override fun background(theme: ThemeData) = Color(0, 0, 0, 0)
    }

    object PRIMARY : ButtonStyle {
        override fun text(theme: ThemeData) = theme.primary1Color
        override fun ripple(theme: ThemeData) = theme.textColor
        override fun background(theme: ThemeData) = Color(0, 0, 0, 0)
    }

    object ACCENT : ButtonStyle {
        override fun text(theme: ThemeData) = theme.accent1Color
        override fun ripple(theme: ThemeData) = theme.textColor
        override fun background(theme: ThemeData) = Color(0, 0, 0, 0)
    }

    object PRIMARY_INVERSE : ButtonStyle {
        override fun text(theme: ThemeData) = theme.alternateTextColor
        override fun ripple(theme: ThemeData) = theme.textColor
        override fun background(theme: ThemeData) = theme.primary1Color
    }

    object ACCENT_INVERSE : ButtonStyle {
        override fun text(theme: ThemeData) = theme.alternateTextColor
        override fun ripple(theme: ThemeData) = theme.textColor
        override fun background(theme: ThemeData) = theme.accent1Color
    }
}