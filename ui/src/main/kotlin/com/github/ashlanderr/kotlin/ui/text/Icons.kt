package com.github.ashlanderr.kotlin.ui.text

import java.awt.Font
import java.awt.GraphicsEnvironment

object Icons {
    const val FA_SOLID = "Font Awesome 5 Free Solid"

    val TIMES = IconData(0xf00d, FA_SOLID)
    val EXPAND = IconData(0xf065, FA_SOLID)
    val COMPRESS = IconData(0xf066, FA_SOLID)
    val MINUS = IconData(0xf068, FA_SOLID)

    init {
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        ge.registerFont("kotlin-ui/assets/fonts/Font Awesome 5 Brands-Regular-400.otf")
        ge.registerFont("kotlin-ui/assets/fonts/Font Awesome 5 Free-Regular-400.otf")
        ge.registerFont("kotlin-ui/assets/fonts/Font Awesome 5 Free-Solid-900.otf")
    }

    private fun GraphicsEnvironment.registerFont(url: String) {
        ClassLoader.getSystemResource(url).openStream().use { stream ->
            val font = Font.createFont(Font.TRUETYPE_FONT, stream)
            registerFont(font)
        }
    }
}