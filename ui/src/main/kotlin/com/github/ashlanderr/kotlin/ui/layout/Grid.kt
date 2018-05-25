package com.github.ashlanderr.kotlin.ui.layout

import com.github.ashlanderr.kotlin.ui.core.*
import java.awt.Graphics2D
import kotlin.math.max

sealed class Element {
    open var realSize: Double = 0.0
        protected set

    var offset: Double = 0.0
    abstract val weight: Double

    abstract fun reset()
    abstract fun expand(size: Double)
    abstract fun flex(size: Double)

    class Fixed(val value: Double) : Element() {
        override val weight: Double = 0.0

        override fun reset() {
            offset = 0.0
            realSize = value
        }

        override fun expand(size: Double) {}
        override fun flex(size: Double) {}
    }

    class Auto : Element() {
        override val weight: Double = 0.0

        override fun reset() {
            offset = 0.0
            realSize = 0.0
        }

        override fun expand(size: Double) {
            realSize = max(realSize, size)
        }

        override fun flex(size: Double) {}
    }

    class Flex(override val weight: Double) : Element() {
        override fun reset() {
            offset = 0.0
            realSize = 0.0
        }

        override fun expand(size: Double) {}

        override fun flex(size: Double) {
            realSize = size
        }
    }
}

class Cell(
    var column: Int = 0,
    var row: Int = 0,
    var columnSpan: Int = 1,
    var rowSpan: Int = 1,
    var horizontalAlign: HorizontalAlign = HorizontalAlign.STRETCH,
    var verticalAlign: VerticalAlign = VerticalAlign.STRETCH,
    @RxNode var child: Node,
    key: Any? = null
) : AbstractNode(key) {

    private val grid: Grid get() = parent as Grid

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        child.measure(g, w, h)

        grid.columns[column + columnSpan - 1].expand(child.renderWidth)
        grid.rows[row + rowSpan - 1].expand(child.renderHeight)
    }

    fun computeSize(g: Graphics2D) {
        renderWidth = grid.columns.sumSize(column, columnSpan)
        renderHeight = grid.rows.sumSize(row, rowSpan)

        val cw = horizontalAlign.computeWidth(child.renderWidth, Constraint.Max(renderWidth))
        val ch = verticalAlign.computeHeight(child.renderHeight, Constraint.Max(renderHeight))
        child.measure(g, cw, ch)
    }

    override fun arrange(left: Double, top: Double) {
        renderLeft = grid.columns[column].offset + left
        renderTop = grid.rows[row].offset + top

        val cl = horizontalAlign.computeLeft(child.renderWidth, renderWidth)
        val ct = verticalAlign.computeTop(child.renderHeight, renderHeight)
        child.arrange(renderLeft + cl, renderTop + ct)
    }

    override fun render(g: Graphics2D) {
        val clip = g.pushClip(renderLeft.toInt(), renderTop.toInt(), renderWidth.toInt(), renderHeight.toInt())
        child.render(g)
        g.popClip(clip)
    }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }

    override fun childAtPoint(point: Point) = child.takeIf { it.containsPoint(point) }
}

class Grid(
    var columns: List<Element>,
    var rows: List<Element>,
    @RxList var children: MutableList<Cell>,
    key: Any? = null
) : AbstractNode(key) {

    override fun measure(g: Graphics2D, w: Constraint, h: Constraint) {
        columns.forEach(Element::reset)
        rows.forEach(Element::reset)

        val cw = Constraint.Min(w.size)
        val ch = Constraint.Min(h.size)
        children.forEach { it.measure(g, cw, ch) }

        computeFlex(columns, w)
        computeFlex(rows, h)

        children.forEach { it.computeSize(g) }

        renderWidth = columns.sumSize()
        renderHeight = rows.sumSize()
    }

    override fun arrange(left: Double, top: Double) {
        renderLeft = left
        renderTop = top

        arrangeElements(columns)
        arrangeElements(rows)
        children.forEach { it.arrange(left, top) }
    }

    override fun render(g: Graphics2D) {
        for (child in children) child.render(g)
    }

    override fun mount(parent: Node?) {
        this.parent = parent
    }

    override fun unmount() {
        this.parent = null
    }

    override fun childAtPoint(point: Point) = children.childAtPoint(point)

    private fun arrangeElements(elements: List<Element>) {
        var offset = 0.0
        for (element in elements) {
            element.offset = offset
            offset += element.realSize
        }
    }

    private fun computeFlex(elements: List<Element>, full: Constraint) {
        val fixed = elements.sumSize()
        val available = full.compute(fixed, full.size) - fixed
        val totalWeight = elements.sumWeight()
        elements.forEach { it.flex(it.weight / totalWeight * available) }
    }
}

private fun List<Element>.sumSize(fromIndex: Int = 0, count: Int = this.size) = this
    .subList(fromIndex, fromIndex + count)
    .sumByDouble { it.realSize }

private fun List<Element>.sumWeight(fromIndex: Int = 0, count: Int = this.size) = this
    .subList(fromIndex, fromIndex + count)
    .sumByDouble { it.weight }

class ElementsScope(private val items: MutableList<Element>) {
    fun auto() {
        items.add(Element.Auto())
    }

    fun fixed(value: Double) {
        items.add(Element.Fixed(value))
    }

    fun flex(weight: Double) {
        items.add(Element.Flex(weight))
    }
}

inline fun elements(builder: ElementsScope.() -> Unit): List<Element> {
    val items = mutableListOf<Element>()
    ElementsScope(items).apply(builder)
    return items
}