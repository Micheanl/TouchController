package top.fifthlight.combine.core.modifier.drawing

import top.fifthlight.combine.core.input.pointer.PointerIcon
import top.fifthlight.combine.core.layout.measure.Placeable
import top.fifthlight.combine.core.layout.measure.contains
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.node.LayoutNode
import top.fifthlight.combine.core.paint.Canvas
import top.fifthlight.data.Offset

fun Modifier.pointerHoverIcon(pointerIcon: PointerIcon) = then(PointerHoverIcon(pointerIcon))

private data class PointerHoverIcon(val icon: PointerIcon) : DrawModifierNode, Modifier.Node<PointerHoverIcon> {
    override fun renderAfter(canvas: Canvas, wrapperNode: Placeable, node: LayoutNode, cursorPos: Offset) {
        if (cursorPos in wrapperNode) {
            canvas.requestPointerIcon(icon)
        }
    }
}
