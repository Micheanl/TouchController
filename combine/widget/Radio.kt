package top.fifthlight.combine.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import top.fifthlight.combine.core.input.interaction.InteractionSource
import top.fifthlight.combine.core.input.interaction.MutableInteractionSource
import top.fifthlight.combine.core.layout.Alignment
import top.fifthlight.combine.core.layout.Arrangement
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.modifier.focus.focusable
import top.fifthlight.combine.core.modifier.pointer.clickable
import top.fifthlight.combine.core.modifier.pointer.toggleable
import top.fifthlight.combine.core.sound.LocalSoundManager
import top.fifthlight.combine.core.sound.SoundKind
import top.fifthlight.combine.core.sound.SoundManager
import top.fifthlight.combine.core.widget.layout.Row
import top.fifthlight.combine.core.widget.layout.RowScope
import top.fifthlight.combine.theme.LocalTheme
import top.fifthlight.combine.ui.style.OnOffDrawableSet
import top.fifthlight.combine.ui.style.get

@Composable
fun RadioIcon(
    modifier: Modifier = Modifier,
    interactionSource: InteractionSource,
    drawableSet: OnOffDrawableSet = LocalTheme.current.drawables.radio,
    enabled: Boolean = true,
    value: Boolean,
) {
    val currentDrawableSet = drawableSet[value]
    val state by widgetState(interactionSource)
    val drawable = currentDrawableSet.getByState(state, enabled = enabled)

    Icon(
        modifier = modifier,
        drawable = drawable,
    )
}

@Composable
fun RadioBoxItem(
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    value: Boolean,
    onValueChanged: (Boolean) -> Unit,
    enabled: Boolean = true,
    clickSound: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val soundManager: SoundManager = LocalSoundManager.current
    Row(
        modifier = if (enabled) modifier.toggleable(
            interactionSource = interactionSource,
            value = value,
            onValueChanged = {
                if (clickSound) {
                    soundManager.play(SoundKind.BUTTON_PRESS, 1f)
                }
                onValueChanged(it)
            },
        ) else modifier,
        horizontalArrangement = Arrangement.spacedBy(4),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioIcon(
            interactionSource = interactionSource,
            enabled = enabled,
            value = value,
        )
        content()
    }
}

@Composable
fun Radio(
    modifier: Modifier = Modifier,
    drawableSet: OnOffDrawableSet = LocalTheme.current.drawables.radio,
    enabled: Boolean = true,
    value: Boolean,
    onValueChanged: ((Boolean) -> Unit)?,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val modifier = if (onValueChanged == null || !enabled) {
        modifier
    } else {
        Modifier
            .clickable(interactionSource) {
                onValueChanged(!value)
            }
            .focusable(interactionSource)
            .then(modifier)
    }

    RadioIcon(
        modifier = modifier,
        interactionSource = interactionSource,
        drawableSet = drawableSet,
        enabled = enabled,
        value = value,
    )
}
