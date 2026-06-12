package top.fifthlight.combine.theme.blackstone.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import top.fifthlight.combine.core.layout.Alignment
import top.fifthlight.combine.core.layout.Arrangement
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.widget.layout.Row
import top.fifthlight.combine.core.widget.layout.RowScope
import top.fifthlight.combine.theme.LocalTheme
import top.fifthlight.combine.theme.blackstone.BlackstoneTextures
import top.fifthlight.combine.ui.style.ColorThemeSet
import top.fifthlight.combine.ui.style.DrawableSet
import top.fifthlight.combine.ui.style.OnOffDrawableSet
import top.fifthlight.combine.widget.Button
import top.fifthlight.combine.widget.Icon
import top.fifthlight.combine.widget.LocalWidgetState
import top.fifthlight.combine.widget.getByState
import top.fifthlight.data.IntSize

val LocalCheckBoxButtonDrawableSet = staticCompositionLocalOf {
    DrawableSet(
        normal = BlackstoneTextures.widget_checkbox_button_checkbox_button,
        hover = BlackstoneTextures.widget_checkbox_button_checkbox_button_hover,
        active = BlackstoneTextures.widget_checkbox_button_checkbox_button_active,
        disabled = BlackstoneTextures.widget_checkbox_button_checkbox_button_disabled,
    )
}

@Composable
fun CheckBoxButton(
    modifier: Modifier = Modifier,
    drawableSet: DrawableSet = LocalCheckBoxButtonDrawableSet.current,
    checkBoxDrawableSet: OnOffDrawableSet = LocalTheme.current.drawables.checkbox,
    colorThemeSet: ColorThemeSet = LocalTheme.current.colors.button,
    minSize: IntSize = IntSize(48, 20),
    enabled: Boolean = true,
    checked: Boolean = false,
    onClick: () -> Unit,
    clickSound: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) = Button(
    modifier = modifier,
    drawableSet = drawableSet,
    colorThemeSet = colorThemeSet,
    minSize = minSize,
    enabled = enabled,
    onClick = onClick,
    clickSound = clickSound,
    content = {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()

            val state = LocalWidgetState.current
            val drawableSet = if (checked) {
                checkBoxDrawableSet.on
            } else {
                checkBoxDrawableSet.off
            }
            Icon(drawableSet.getByState(state))
        }
    }
)
