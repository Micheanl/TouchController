package top.fifthlight.combine.core.modifier.focus

import top.fifthlight.combine.core.input.focus.LocalFocusManager
import top.fifthlight.combine.core.input.interaction.Interaction
import top.fifthlight.combine.core.input.interaction.MutableInteractionSource
import top.fifthlight.combine.core.input.pointer.PointerEvent
import top.fifthlight.combine.core.input.pointer.PointerEventType
import top.fifthlight.combine.core.layout.measure.Placeable
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.modifier.pointer.PointerInputModifierNode
import top.fifthlight.combine.core.node.AttachListenerModifierNode
import top.fifthlight.combine.core.node.LayoutNode
import top.fifthlight.combine.core.node.plus

sealed class FocusInteraction : Interaction {
    data object Blur : FocusInteraction()
    data object Focus : FocusInteraction()
}

fun Modifier.focusable(
    interactionSource: MutableInteractionSource? = null,
) = then(
    FocusableModifierNode(
        interactionSource = interactionSource,
    )
)

data class FocusableModifierNode(
    val interactionSource: MutableInteractionSource?,
) : Modifier.Node<FocusableModifierNode>, FocusStateListenerModifierNode, PointerInputModifierNode,
    AttachListenerModifierNode {
    override fun onFocusStateChanged(focused: Boolean) {
        interactionSource?.tryEmit(if (focused) FocusInteraction.Focus else FocusInteraction.Blur)
    }

    override fun onPointerEvent(
        event: PointerEvent,
        node: Placeable,
        layoutNode: LayoutNode,
        children: (PointerEvent) -> Boolean,
    ): Boolean {
        if (event.type == PointerEventType.Press) {
            layoutNode.compositionLocalMap[LocalFocusManager].requestFocus(layoutNode)
            children(event)
            return true
        }
        return false
    }

    override fun onAttachedToNode(node: LayoutNode) {
        node.focusable = true
    }

    companion object {
        private val wrapperFactory =
            PointerInputModifierNode.wrapperFactory + FocusStateListenerModifierNode.wrapperFactory
    }

    override val wrapperFactory
        get() = Companion.wrapperFactory
}
