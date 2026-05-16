package top.fifthlight.combine.widget.ui

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
import top.fifthlight.combine.ui.style.DrawableSet

data class CheckBoxDrawableSet(
    val unchecked: DrawableSet,
    val checked: DrawableSet,
) {
    companion object {
        val current
            @Composable get() = LocalTheme.current.drawables.let { drawables ->
                CheckBoxDrawableSet(
                    unchecked = drawables.uncheckedCheckBox,
                    checked = drawables.checkboxChecked,
                )
            }
    }
}

@Composable
fun CheckBoxIcon(
    modifier: Modifier = Modifier,
    interactionSource: InteractionSource,
    drawableSet: CheckBoxDrawableSet = CheckBoxDrawableSet.current,
    value: Boolean,
) {
    val currentDrawableSet = if (value) {
        drawableSet.checked
    } else {
        drawableSet.unchecked
    }
    val state by widgetState(interactionSource)
    val drawable = currentDrawableSet.getByState(state)

    Icon(
        modifier = modifier,
        drawable = drawable,
    )
}

@Composable
fun CheckBoxItem(
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    value: Boolean,
    onValueChanged: (Boolean) -> Unit,
    clickSound: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val soundManager: SoundManager = LocalSoundManager.current
    Row(
        modifier = Modifier.toggleable(
            interactionSource = interactionSource,
            value = value,
            onValueChanged = {
                if (clickSound) {
                    soundManager.play(SoundKind.BUTTON_PRESS, 1f)
                }
                onValueChanged(it)
            },
        ),
        horizontalArrangement = Arrangement.spacedBy(4),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CheckBoxIcon(
            interactionSource = interactionSource,
            value = value,
        )
        content()
    }
}

@Composable
fun CheckBox(
    modifier: Modifier = Modifier,
    drawableSet: CheckBoxDrawableSet = CheckBoxDrawableSet.current,
    value: Boolean,
    onValueChanged: ((Boolean) -> Unit)?,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val modifier = if (onValueChanged == null) {
        modifier
    } else {
        Modifier
            .clickable(interactionSource) {
                onValueChanged(!value)
            }
            .focusable(interactionSource)
            .then(modifier)
    }

    CheckBoxIcon(
        modifier = modifier,
        interactionSource = interactionSource,
        drawableSet = drawableSet,
        value = value,
    )
}
