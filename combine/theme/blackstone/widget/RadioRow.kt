package top.fifthlight.combine.theme.blackstone.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import top.fifthlight.combine.core.layout.Alignment
import top.fifthlight.combine.core.layout.Arrangement
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.modifier.drawing.border
import top.fifthlight.combine.core.modifier.placement.padding
import top.fifthlight.combine.core.paint.Drawable
import top.fifthlight.combine.core.widget.layout.Row
import top.fifthlight.combine.core.widget.layout.RowScope
import top.fifthlight.combine.theme.blackstone.BlackstoneTextures
import top.fifthlight.combine.ui.style.ColorTheme
import top.fifthlight.combine.ui.style.LocalColorTheme

val LocalRadioRowBorderDrawable = staticCompositionLocalOf { BlackstoneTextures.widget_background_float_window }

@Composable
fun RadioRow(
    modifier: Modifier = Modifier,
    border: Drawable = LocalRadioRowBorderDrawable.current,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .padding(4)
            .border(border)
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(4),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(
            LocalColorTheme provides ColorTheme.light,
        ) {
            content()
        }
    }
}
