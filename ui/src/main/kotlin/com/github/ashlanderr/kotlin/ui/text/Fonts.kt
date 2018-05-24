package com.github.ashlanderr.kotlin.ui.text

import java.awt.Font
import java.awt.GraphicsEnvironment

object Fonts {
    val AWESOME_SOLID = "Font Awesome 5 Free Solid"
    val ROBOTO = "Roboto"

    init {
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        ge.registerFont("kotlin-ui/assets/fonts/Font Awesome 5 Brands-Regular-400.otf")
        ge.registerFont("kotlin-ui/assets/fonts/Font Awesome 5 Free-Regular-400.otf")
        ge.registerFont("kotlin-ui/assets/fonts/Font Awesome 5 Free-Solid-900.otf")
        ge.registerFont("kotlin-ui/assets/fonts/Roboto-Regular.ttf")
    }

    private fun GraphicsEnvironment.registerFont(url: String) {
        ClassLoader.getSystemResource(url).openStream().use { stream ->
            val font = Font.createFont(Font.TRUETYPE_FONT, stream)
            registerFont(font)
        }
    }
}