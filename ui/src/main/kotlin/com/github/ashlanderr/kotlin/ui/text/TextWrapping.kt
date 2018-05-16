package com.github.ashlanderr.kotlin.ui.text

import java.awt.FontMetrics

sealed class TextWrapping {
    abstract fun split(text: String, fm: FontMetrics, maxWidth: Int): List<Int>

    object NO_WRAP : TextWrapping() {
        override fun split(text: String, fm: FontMetrics, maxWidth: Int): List<Int> {
            return listOf(0, text.length)
        }
    }

    object BREAK_WORD : TextWrapping() {
        override fun split(text: String, fm: FontMetrics, maxWidth: Int): List<Int> {
            var width = 0
            val breaks = ArrayList<Int>()
            breaks.add(0)

            for (i in text.indices) {
                val ch = text[i]
                val chWidth = fm.charWidth(ch)

                if (width > 0.0 && width + chWidth > maxWidth) {
                    width = chWidth
                    breaks.add(i)
                } else {
                    width += chWidth
                }
            }

            breaks.add(text.length)
            return breaks
        }
    }

    object WORD_WRAP : TextWrapping() {
        override fun split(text: String, fm: FontMetrics, maxWidth: Int): List<Int> {
            var width = 0
            var lastSpace = -1
            val breaks = ArrayList<Int>()
            var i = 0
            breaks.add(0)

            while (i < text.length) {
                val ch = text[i]
                val chWidth = fm.charWidth(ch)
                val isSpace = ch.isWhitespace()

                if (isSpace) lastSpace = i

                if (width + chWidth > maxWidth && lastSpace >= 0 && width > 0.0 && !isSpace) {
                    width = 0
                    i = lastSpace + 1
                    lastSpace = -1
                    breaks.add(i)
                } else {
                    width += chWidth
                    i += 1
                }
            }

            breaks.add(text.length)
            return breaks
        }
    }
}