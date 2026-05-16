package top.fifthlight.combine.core.modifier.placement

import top.fifthlight.combine.core.layout.measure.Placeable
import top.fifthlight.combine.core.modifier.Modifier

fun Modifier.onPlaced(onPlaced: (Placeable) -> Unit) = then(OnPlacedNode(onPlaced))

private data class OnPlacedNode(
    val callback: (Placeable) -> Unit
) : PlaceListeningModifierNode, Modifier.Node<OnPlacedNode> {
    override fun onPlaced(placeable: Placeable) {
        callback.invoke(placeable)
    }
}
