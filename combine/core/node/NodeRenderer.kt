package top.fifthlight.combine.core.node

import top.fifthlight.combine.core.layout.measure.Placeable
import top.fifthlight.combine.core.paint.Canvas

fun interface NodeRenderer {
    fun render(canvas: Canvas, node: Placeable)

    companion object EmptyRenderer : NodeRenderer {
        override fun render(canvas: Canvas, node: Placeable) = Unit
    }
}
