package top.fifthlight.combine.core.modifier.input

import top.fifthlight.combine.core.input.focus.FocusStateListener
import top.fifthlight.combine.core.input.key.KeyEventReceiver
import top.fifthlight.combine.core.input.pointer.PointerEventReceiver
import top.fifthlight.combine.core.input.text.TextInputReceiver
import top.fifthlight.combine.core.node.LayoutNode
import top.fifthlight.combine.core.node.WrapperFactory
import top.fifthlight.combine.core.node.WrapperLayoutNode
import top.fifthlight.combine.core.node.WrapperModifierNode

interface TextInputModifierNode : TextInputReceiver, WrapperModifierNode {
    companion object {
        private class TextInputWrapperNode(
            node: LayoutNode,
            children: WrapperLayoutNode,
            private val modifierNode: TextInputModifierNode,
        ) : WrapperLayoutNode.PositionWrapper(node, children),
            PointerEventReceiver by children,
            FocusStateListener by children,
            KeyEventReceiver by children {

            override fun onTextInput(string: String) = modifierNode.onTextInput(string)
        }

        val wrapperFactory = WrapperFactory<TextInputModifierNode> { node, children, modifier ->
            TextInputWrapperNode(node, children, modifier)
        }
    }

    override val wrapperFactory: WrapperFactory<*>
        get() = Companion.wrapperFactory
}
