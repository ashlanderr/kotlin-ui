package com.github.ashlanderr.kotlin.ui.gallery

import com.github.ashlanderr.kotlin.ui.core.Application
import com.github.ashlanderr.kotlin.ui.core.Builder
import com.github.ashlanderr.kotlin.ui.core.StatelessComponent
import com.github.ashlanderr.kotlin.ui.graphics.rect
import com.github.ashlanderr.kotlin.ui.layout.*
import com.github.ashlanderr.kotlin.ui.material.ButtonStyle
import com.github.ashlanderr.kotlin.ui.material.Theme
import com.github.ashlanderr.kotlin.ui.material.ThemeData
import com.github.ashlanderr.kotlin.ui.material.flatButton
import com.github.ashlanderr.kotlin.ui.text.TextStyle
import com.github.ashlanderr.kotlin.ui.text.textBlock
import java.awt.Color
import javax.swing.JFrame

private val LIGHT_THEME = ThemeData()

private val DARK_THEME = ThemeData(
    primary1Color = Color(0,151,167),
    primary2Color = Color(0,151,167),
    primary3Color = Color(117,117,117),
    accent1Color = Color(255,64,129),
    accent2Color = Color(245,0,87),
    accent3Color = Color(255,128,171),
    textColor = Color(255, 255, 255, 255),
    secondaryTextColor = Color(55, 255, 255, 178),
    alternateTextColor = Color(48,48,48),
    canvasColor = Color(48,48,48),
    borderColor = Color(255, 255, 255, 76),
    disabledColor = Color(255, 255, 255, 76),
    shadowColor = Color(0, 0, 0)
)

class Gallery : StatelessComponent() {
    override fun render() = padding {
        padding = Indent.all(32.0)
        child = column {
            +category("Flat buttons") {
                +galleryFlatButton(ButtonStyle.DEFAULT, "DEFAULT")
                +galleryFlatButton(ButtonStyle.PRIMARY, "PRIMARY")
                +galleryFlatButton(ButtonStyle.ACCENT, "ACCENT")
                +galleryFlatButton(ButtonStyle.ACCENT, "DISABLED", false)
            }
        }
    }

    private inline fun category(title: String, crossinline builder: Builder<Column>) = column {
        align = HorizontalAlign.STRETCH
        +textBlock {
            style = TextStyle(fontSize = 24, color = Theme.of(this@Gallery).textColor)
            text = title
        }
        +sizedBox { height = 12.0 }
        builder()
    }

    private fun galleryFlatButton(style: ButtonStyle, title: String, enabled: Boolean = true) = padding {
        padding = Indent.symetric(0.0, 0.0)
        child = flatButton {
            this.enabled = enabled
            this.style = style
            this.content = padding {
                padding = Indent.symetric(16.0, 8.0)
                child = center {
                    child = textBlock {
                        text = title
                    }
                }
            }
        }
    }
}

class GalleryApp : Application() {
    override fun render() = grid {
        columns {
            flex(1.0)
            flex(1.0)
        }
        rows {
            flex(1.0)
        }
        cell {
            column = 0
            row = 0
            child = Theme(
                data = LIGHT_THEME,
                child = stack {
                    +rect { stroke = null; fill = null }
                    +Gallery()
                }
            )
        }
        cell {
            column = 1
            row = 0
            child = Theme(
                data = DARK_THEME,
                child = stack {
                    +rect { stroke = null; fill = null }
                    +Gallery()
                }
            )
        }
    }
}

fun main(args: Array<String>) {
    val app = GalleryApp()
    app.setBounds(200, 200, 800, 600)
    app.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    app.isVisible = true
}