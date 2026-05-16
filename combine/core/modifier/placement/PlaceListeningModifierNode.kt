package top.fifthlight.combine.core.modifier.placement

import top.fifthlight.combine.core.input.focus.FocusStateListener
import top.fifthlight.combine.core.input.key.KeyEventReceiver
import top.fifthlight.combine.core.input.pointer.PointerEventReceiver
import top.fifthlight.combine.core.input.text.TextInputReceiver
import top.fifthlight.combine.core.layout.constraints.Constraints
import top.fifthlight.combine.core.layout.measure.Placeable
import top.fifthlight.combine.core.node.LayoutNode
import top.fifthlight.combine.core.node.WrapperFactory
import top.fifthlight.combine.core.node.WrapperLayoutNode
import top.fifthlight.combine.core.node.WrapperModifierNode

interface PlaceListeningModifierNode : WrapperModifierNode {
    fun onPlaced(placeable: Placeable)

    companion object {
        private class OnPlacedWrapperNode(
            node: LayoutNode,
            children: WrapperLayoutNode,
            private val modifierNode: PlaceListeningModifierNode,
        ) : WrapperLayoutNode.PositionWrapper(node, children),
            PointerEventReceiver by children,
            FocusStateListener by children,
            TextInputReceiver by children,
            KeyEventReceiver by children {

            override fun measure(constraints: Constraints): Placeable {
                val result = super.measure(constraints)
                return object : Placeable by result {
                    override fun placeAt(x: Int, y: Int) {
                        result.placeAt(x, y)
                        modifierNode.onPlaced(node)
                    }
                }
            }
        }

        val wrapperFactory = WrapperFactory<PlaceListeningModifierNode> { node, children, modifier ->
            OnPlacedWrapperNode(node, children, modifier)
        }
    }

    override val wrapperFactory: WrapperFactory<*>
        get() = Companion.wrapperFactory
}
