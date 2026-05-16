package top.fifthlight.combine.core.modifier.key

import top.fifthlight.combine.core.input.key.KeyEvent
import top.fifthlight.combine.core.input.key.KeyEventReceiver
import top.fifthlight.combine.core.modifier.Modifier

fun Modifier.onKeyEvent(handler: KeyEventReceiver) = then(KeyEventReceiverModifierNode(handler))

private data class KeyEventReceiverModifierNode(
    val handler: KeyEventReceiver
) : Modifier.Node<KeyEventReceiverModifierNode>, KeyInputModifierNode {
    override fun onKeyEvent(event: KeyEvent) = handler.onKeyEvent(event)
}
