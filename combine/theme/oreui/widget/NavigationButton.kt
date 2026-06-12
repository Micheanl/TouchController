package top.fifthlight.combine.theme.oreui.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.staticCompositionLocalOf
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.paint.Colors
import top.fifthlight.combine.core.widget.layout.BoxScope
import top.fifthlight.combine.theme.oreui.OreUITextures
import top.fifthlight.combine.theme.oreui.OutlineDrawable
import top.fifthlight.combine.ui.style.*
import top.fifthlight.combine.widget.CheckButton
import top.fifthlight.data.IntPadding
import top.fifthlight.data.IntSize

val LocalNavigationButtonTheme = staticCompositionLocalOf { NavigationButtonTheme.default }

data class NavigationButtonTheme(
    val drawableSet: OnOffDrawableSet = OnOffDrawableSet(
        off = DrawableSet(
            normal = OreUITextures.widget_navigation_button_navigation_button_off,
            focus = OutlineDrawable(OreUITextures.widget_navigation_button_navigation_button_off),
            hover = OreUITextures.widget_navigation_button_navigation_button_off_hover,
            active = OreUITextures.widget_navigation_button_navigation_button_off_active,
            disabled = OreUITextures.widget_navigation_button_navigation_button_off_disabled,
        ),
        on = DrawableSet(
            normal = OreUITextures.widget_navigation_button_navigation_button_on,
            focus = OutlineDrawable(OreUITextures.widget_navigation_button_navigation_button_on),
            hover = OreUITextures.widget_navigation_button_navigation_button_on_hover,
            active = OreUITextures.widget_navigation_button_navigation_button_on_active,
            disabled = OreUITextures.widget_navigation_button_navigation_button_on_disabled,
        ),
    ),
    val colorThemeSet: OnOffColorSet = OnOffColorSet(
        off = ColorThemeSet(
            normal = ColorTheme.dark,
            disabled = ColorTheme.dark.copy(foreground = Colors.SECONDARY_WHITE),
        ),
    ),
    val textStyleSet: TextStyleSet = EmptyTextStyleSet,
) {
    companion object {
        val default = NavigationButtonTheme()
    }
}

@NonRestartableComposable
@Composable
fun NavigationButton(
    modifier: Modifier = Modifier,
    focusable: Boolean = true,
    theme: NavigationButtonTheme = LocalNavigationButtonTheme.current,
    checked: Boolean = false,
    padding: IntPadding = IntPadding(left = 4, right = 4),
    minSize: IntSize = IntSize(48, 20),
    enabled: Boolean = true,
    onClick: () -> Unit,
    clickSound: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) = CheckButton(
    modifier = modifier,
    focusable = focusable,
    drawableSet = theme.drawableSet,
    colorThemeSet = theme.colorThemeSet,
    textStyleSet = theme.textStyleSet,
    checked = checked,
    minSize = minSize,
    padding = padding,
    enabled = enabled,
    onClick = onClick,
    clickSound = clickSound,
    content = content
)
