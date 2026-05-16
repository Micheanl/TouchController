package top.fifthlight.combine.core.modifier.input

import top.fifthlight.combine.core.input.text.TextInputReceiver
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.node.WrapperFactory

fun Modifier.textInput(handler: TextInputReceiver) = then(TextInputReceiverModifierNode(handler))

private data class TextInputReceiverModifierNode(
    val handler: TextInputReceiver
) : Modifier.Node<TextInputReceiverModifierNode>, TextInputModifierNode {
    override fun onTextInput(string: String) = handler.onTextInput(string)

    override val wrapperFactory: WrapperFactory<*>
        get() = TextInputModifierNode.wrapperFactory
}
