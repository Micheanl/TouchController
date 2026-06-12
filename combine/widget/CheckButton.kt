package top.fifthlight.combine.widget

import androidx.compose.runtime.Composable
import top.fifthlight.combine.core.modifier.Modifier
import top.fifthlight.combine.core.widget.layout.BoxScope
import top.fifthlight.combine.ui.style.OnOffColorSet
import top.fifthlight.combine.ui.style.OnOffDrawableSet
import top.fifthlight.combine.ui.style.TextStyleSet
import top.fifthlight.combine.ui.style.get
import top.fifthlight.data.IntPadding
import top.fifthlight.data.IntSize

@Composable
fun CheckButton(
    modifier: Modifier = Modifier,
    focusable: Boolean = true,
    drawableSet: OnOffDrawableSet,
    colorThemeSet: OnOffColorSet,
    textStyleSet: TextStyleSet,
    checked: Boolean = false,
    padding: IntPadding = IntPadding(left = 4, right = 4),
    minSize: IntSize = IntSize(48, 20),
    enabled: Boolean = true,
    onClick: () -> Unit,
    clickSound: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) = Button(
    modifier = modifier,
    focusable = focusable,
    drawableSet = drawableSet[checked],
    colorThemeSet = colorThemeSet[checked],
    textStyleSet = textStyleSet,
    minSize = minSize,
    padding = padding,
    enabled = enabled,
    onClick = onClick,
    clickSound = clickSound,
    content = content
)
