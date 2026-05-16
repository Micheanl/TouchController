package top.fifthlight.combine.core.modifier.pointer

import top.fifthlight.combine.core.input.pointer.PointerEvent
import top.fifthlight.combine.core.layout.measure.Placeable
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.node.LayoutNode

fun Modifier.onPointerInput(receiver: Placeable.(PointerEvent) -> Boolean) =
    then(PointerInputReceiverModifierNode(receiver))

private class PointerInputReceiverModifierNode(
    private val receiver: Placeable.(PointerEvent) -> Boolean
) : Modifier.Node<PointerInputReceiverModifierNode>,
    PointerInputModifierNode {
    override fun onPointerEvent(
        event: PointerEvent,
        node: Placeable,
        layoutNode: LayoutNode,
        children: (PointerEvent) -> Boolean
    ): Boolean =
        receiver.invoke(node, event)
}
