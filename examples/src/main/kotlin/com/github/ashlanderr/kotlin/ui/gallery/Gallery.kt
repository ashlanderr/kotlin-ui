package com.github.ashlanderr.kotlin.ui.gallery

import com.github.ashlanderr.kotlin.ui.core.Application
import com.github.ashlanderr.kotlin.ui.core.Node
import com.github.ashlanderr.kotlin.ui.core.StatelessComponent
import com.github.ashlanderr.kotlin.ui.graphics.Rect
import com.github.ashlanderr.kotlin.ui.layout.*
import com.github.ashlanderr.kotlin.ui.material.*
import com.github.ashlanderr.kotlin.ui.text.TextBlock
import java.awt.Color
import javax.swing.JFrame

private val LIGHT_THEME = ThemeData()

private val DARK_THEME = ThemeData(
    primary1Color = Color(0, 151, 167),
    primary2Color = Color(0, 151, 167),
    primary3Color = Color(117, 117, 117),
    accent1Color = Color(255, 64, 129),
    accent2Color = Color(245, 0, 87),
    accent3Color = Color(255, 128, 171),
    textColor = Color(255, 255, 255, 255),
    secondaryTextColor = Color(55, 255, 255, 178),
    alternateTextColor = Color(48, 48, 48),
    canvasColor = Color(48, 48, 48),
    borderColor = Color(255, 255, 255, 76),
    disabledColor = Color(255, 255, 255, 76),
    shadowColor = Color(0, 0, 0)
)

class Gallery : StatelessComponent() {
    override fun render() = Padding(
        padding = Indent.all(32.0),
        child = Column(
            children = mutableListOf(
                category("Flat buttons",
                    galleryFlatButton(ButtonStyle.DEFAULT, "DEFAULT"),
                    galleryFlatButton(ButtonStyle.PRIMARY, "PRIMARY"),
                    galleryFlatButton(ButtonStyle.ACCENT, "ACCENT"),
                    galleryFlatButton(ButtonStyle.ACCENT, "DISABLED", false)
                )
            )
        )
    )

    private fun category(title: String, vararg children: Node) = Column(
        align = HorizontalAlign.STRETCH,
        children = mutableListOf(
            TextBlock(
                fontSize = 24.0,
                text = title
            ),
            SizedBox(width = 0.0, height = 12.0),
            *children
        )
    )

    private fun galleryFlatButton(style: ButtonStyle, title: String, enabled: Boolean = true) = Padding(
        padding = Indent.symmetric(0.0, 0.0),
        child = FlatButton(
            enabled = enabled,
            style = style,
            content = Padding(
                padding = Indent.symmetric(16.0, 8.0),
                child = Center(
                    child = TextBlock(
                        text = title
                    )
                )
            )
        )
    )
}

class GalleryApp : Application() {
    override fun render() = Window(
        title = TextBlock(
            text = "Material gallery"
        ),
        content = Grid(
            columns = elements {
                flex(1.0)
                flex(1.0)
            },
            rows = elements {
                flex(1.0)
            },
            children = mutableListOf(
                themedGallery(0, LIGHT_THEME),
                themedGallery(1, DARK_THEME)
            )
        )
    )

    private fun themedGallery(column: Int, theme: ThemeData): Cell {
        return Cell(
            column = column,
            child = Theme(
                data = theme,
                child = Stack(
                    children = mutableListOf(
                        Rect(stroke = null, fill = null),
                        Gallery()
                    )
                )
            )
        )
    }
}

fun main(args: Array<String>) {
    val app = GalleryApp()
    app.setBounds(200, 200, 800, 600)
    app.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    app.isUndecorated = true
    app.isVisible = true
}