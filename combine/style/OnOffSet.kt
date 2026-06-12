package top.fifthlight.combine.ui.style

data class OnOffSet<T>(
    val off: T,
    val on: T = off,
)

operator fun <T> OnOffSet<T>.get(checked: Boolean): T = if (checked) on else off

typealias OnOffDrawableSet = OnOffSet<DrawableSet>

val EmptyOnOffDrawableSet = OnOffDrawableSet(EmptyDrawableSet)

typealias OnOffColorSet = OnOffSet<ColorThemeSet>
