package top.fifthlight.combine.core.modifier.pointer

import androidx.compose.runtime.Composable
import top.fifthlight.combine.core.input.focus.LocalFocusManager
import top.fifthlight.combine.core.input.pointer.PointerEvent
import top.fifthlight.combine.core.input.pointer.PointerEventType
import top.fifthlight.combine.core.layout.measure.Placeable
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.node.LayoutNode

@Composable
fun Modifier.consumePress(blur: Boolean = true, onPress: () -> Unit = {}) =
    LocalFocusManager.current.let { focusManager ->
        then(PressConsumerModifierNode {
            if (blur) {
                focusManager.requestBlur()
            }
            onPress()
        })
    }

private data class PressConsumerModifierNode(
    val onPress: () -> Unit
) : Modifier.Node<PressConsumerModifierNode>, PointerInputModifierNode {
    override fun onPointerEvent(
        event: PointerEvent,
        node: Placeable,
        layoutNode: LayoutNode,
        children: (PointerEvent) -> Boolean
    ): Boolean {
        if (event.type == PointerEventType.Press) {
            if (!children(event)) {
                onPress()
            }
            return true
        } else {
            return false
        }
    }
}
