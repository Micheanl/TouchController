package top.fifthlight.combine.core.modifier.key

import top.fifthlight.combine.core.input.focus.FocusStateListener
import top.fifthlight.combine.core.input.key.KeyEvent
import top.fifthlight.combine.core.input.key.KeyEventReceiver
import top.fifthlight.combine.core.input.pointer.PointerEventReceiver
import top.fifthlight.combine.core.input.text.TextInputReceiver
import top.fifthlight.combine.core.node.LayoutNode
import top.fifthlight.combine.core.node.WrapperFactory
import top.fifthlight.combine.core.node.WrapperLayoutNode
import top.fifthlight.combine.core.node.WrapperModifierNode

interface KeyInputModifierNode : KeyEventReceiver, WrapperModifierNode {
    companion object {
        private class KeyInputWrapperNode(
            node: LayoutNode,
            children: WrapperLayoutNode,
            private val modifierNode: KeyInputModifierNode,
        ) : WrapperLayoutNode.PositionWrapper(node, children),
            PointerEventReceiver by children,
            FocusStateListener by children,
            TextInputReceiver by children {

            override fun onKeyEvent(event: KeyEvent) = modifierNode.onKeyEvent(event)
        }

        val wrapperFactory = WrapperFactory<KeyInputModifierNode> { node, children, modifier ->
            KeyInputWrapperNode(node, children, modifier)
        }
    }

    override val wrapperFactory: WrapperFactory<*>
        get() = Companion.wrapperFactory
}
