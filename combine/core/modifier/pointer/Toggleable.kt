package top.fifthlight.combine.core.modifier.pointer

import androidx.compose.runtime.Composable
import top.fifthlight.combine.core.input.interaction.MutableInteractionSource
import top.fifthlight.combine.core.modifier.Modifier

@Composable
fun Modifier.toggleable(
    interactionSource: MutableInteractionSource? = null,
    value: Boolean,
    onValueChanged: (Boolean) -> Unit,
) = clickable(interactionSource) {
    onValueChanged(!value)
}
