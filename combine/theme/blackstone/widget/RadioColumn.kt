package top.fifthlight.combine.theme.blackstone.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import top.fifthlight.combine.core.layout.Arrangement
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.modifier.drawing.border
import top.fifthlight.combine.core.modifier.placement.padding
import top.fifthlight.combine.core.paint.Drawable
import top.fifthlight.combine.core.widget.layout.Column
import top.fifthlight.combine.core.widget.layout.ColumnScope
import top.fifthlight.combine.theme.blackstone.BlackstoneTextures
import top.fifthlight.combine.ui.style.ColorTheme
import top.fifthlight.combine.ui.style.LocalColorTheme

val LocalRadioColumnBorderDrawable = staticCompositionLocalOf { BlackstoneTextures.widget_background_float_window }

@Composable
fun RadioColumn(
    modifier: Modifier = Modifier,
    border: Drawable = LocalRadioColumnBorderDrawable.current,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .padding(4)
            .border(border)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(4),
    ) {
        CompositionLocalProvider(
            LocalColorTheme provides ColorTheme.light,
        ) {
            content()
        }
    }
}
