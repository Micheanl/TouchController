package top.fifthlight.combine.core.modifier.focus

import top.fifthlight.combine.core.input.focus.FocusStateListener
import top.fifthlight.combine.core.input.key.KeyEventReceiver
import top.fifthlight.combine.core.input.pointer.PointerEventReceiver
import top.fifthlight.combine.core.input.text.TextInputReceiver
import top.fifthlight.combine.core.node.*

interface FocusStateListenerModifierNode : FocusStateListener, AttachListenerModifierNode, WrapperModifierNode {
    override fun onAttachedToNode(node: LayoutNode) = Unit

    companion object {
        private class FocusStateWrapperNode(
            node: LayoutNode,
            children: WrapperLayoutNode,
            private val modifierNode: FocusStateListenerModifierNode,
        ) : WrapperLayoutNode.PositionWrapper(node, children),
            PointerEventReceiver by children,
            TextInputReceiver by children,
            KeyEventReceiver by children {

            override fun onFocusStateChanged(focused: Boolean) {
                modifierNode.onFocusStateChanged(focused)
                children.onFocusStateChanged(focused)
            }
        }

        val wrapperFactory = WrapperFactory<FocusStateListenerModifierNode> { node, children, modifier ->
            FocusStateWrapperNode(node, children, modifier)
        }
    }

    override val wrapperFactory: WrapperFactory<*>
        get() = Companion.wrapperFactory
}
