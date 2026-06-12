package top.fifthlight.combine.example.widgetfactory.common.category

import androidx.compose.runtime.Composable
import top.fifthlight.combine.theme.Theme

abstract class WidgetCategory {
    abstract val name: String

    open val themes: Set<Theme>?
        get() = null

    @Composable
    abstract fun Interface()
}
