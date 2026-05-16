package top.fifthlight.combine.widget.ui

import androidx.compose.runtime.*
import top.fifthlight.combine.core.input.interaction.MutableInteractionSource
import top.fifthlight.combine.core.layout.Alignment
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.modifier.drawing.border
import top.fifthlight.combine.core.modifier.focus.focusable
import top.fifthlight.combine.core.modifier.placement.minSize
import top.fifthlight.combine.core.modifier.placement.padding
import top.fifthlight.combine.core.modifier.pointer.clickable
import top.fifthlight.combine.core.paint.Colors
import top.fifthlight.combine.core.sound.LocalSoundManager
import top.fifthlight.combine.core.sound.SoundKind
import top.fifthlight.combine.core.widget.layout.Box
import top.fifthlight.combine.core.widget.layout.BoxScope
import top.fifthlight.combine.theme.LocalTheme
import top.fifthlight.combine.ui.style.ColorTheme
import top.fifthlight.combine.ui.style.DrawableSet
import top.fifthlight.combine.ui.style.LocalColorTheme
import top.fifthlight.data.IntPadding
import top.fifthlight.data.IntSize

@NonSkippableComposable
@Composable
fun GuideButton(
    modifier: Modifier = Modifier,
    focusable: Boolean = true,
    drawableSet: DrawableSet = LocalTheme.current.drawables.guideButton,
    colorTheme: ColorTheme = LocalTheme.current.colors.guideButton,
    minSize: IntSize = IntSize(48, 20),
    enabled: Boolean = true,
    onClick: () -> Unit,
    clickSound: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    Button(
        modifier = modifier,
        focusable = focusable,
        drawableSet = drawableSet,
        colorTheme = colorTheme,
        minSize = minSize,
        enabled = enabled,
        onClick = onClick,
        clickSound = clickSound,
        content = content
    )
}

@NonSkippableComposable
@Composable
fun WarningButton(
    modifier: Modifier = Modifier,
    focusable: Boolean = true,
    drawableSet: DrawableSet = LocalTheme.current.drawables.warningButton,
    colorTheme: ColorTheme = LocalTheme.current.colors.warningButton,
    minSize: IntSize = IntSize(48, 20),
    enabled: Boolean = true,
    onClick: () -> Unit,
    clickSound: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    Button(
        modifier = modifier,
        focusable = focusable,
        drawableSet = drawableSet,
        colorTheme = colorTheme,
        minSize = minSize,
        enabled = enabled,
        onClick = onClick,
        clickSound = clickSound,
        content = content
    )
}

@Composable
fun Button(
    modifier: Modifier = Modifier,
    focusable: Boolean = true,
    drawableSet: DrawableSet = LocalTheme.current.drawables.button,
    colorTheme: ColorTheme = LocalTheme.current.colors.button,
    minSize: IntSize = IntSize(48, 20),
    padding: IntPadding = IntPadding(left = 4, right = 4),
    enabled: Boolean = true,
    onClick: () -> Unit,
    clickSound: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    val soundManager = LocalSoundManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val state by widgetState(interactionSource)
    val drawable = drawableSet.getByState(state, enabled = enabled)

    val clickableModifier = Modifier.clickable(interactionSource) {
        if (clickSound) {
            soundManager.play(SoundKind.BUTTON_PRESS, 1f)
        }
        onClick()
    }
    val focusableModifier = Modifier.focusable(interactionSource)

    fun Modifier.then(modifier: Modifier?) = if (modifier != null) {
        then(modifier)
    } else {
        this
    }

    Box(
        modifier = Modifier
            .padding(padding)
            .border(drawable)
            .minSize(minSize)
            .then(clickableModifier.takeIf { enabled })
            .then(focusableModifier.takeIf { enabled && focusable })
            .then(modifier),
        alignment = Alignment.Center,
    ) {
        var colorTheme = colorTheme
        if (!enabled) {
            colorTheme = colorTheme.copy(foreground = Colors.SECONDARY_WHITE)
        }
        CompositionLocalProvider(
            LocalColorTheme provides colorTheme,
            LocalWidgetState provides state,
        ) {
            content()
        }
    }
}
