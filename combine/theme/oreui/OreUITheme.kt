package top.fifthlight.combine.theme.oreui

import top.fifthlight.combine.core.paint.*
import top.fifthlight.combine.theme.Theme
import top.fifthlight.combine.ui.style.*
import top.fifthlight.data.IntOffset
import top.fifthlight.data.IntPadding
import top.fifthlight.data.IntRect
import top.fifthlight.data.IntSize
import kotlin.math.ceil
import kotlin.math.roundToInt

internal data class OutlineDrawable(
    val inner: Drawable,
    val color: Color = Colors.WHITE,
    val innerPadding: IntPadding = IntPadding.ZERO,
) : Drawable {
    override val size
        get() = inner.size
    override val padding: IntPadding
        get() = inner.padding

    override fun draw(
        canvas: Canvas,
        dstRect: IntRect,
        tint: Color,
    ) {
        inner.draw(
            canvas = canvas,
            dstRect = dstRect,
            tint = tint,
        )
        canvas.drawRect(
            offset = dstRect.offset + IntOffset(
                x = -1 + innerPadding.left,
                y = -1 + innerPadding.top,
            ),
            size = dstRect.size + IntSize(
                width = 2 - innerPadding.width,
                height = 2 - innerPadding.height,
            ),
            color = color,
        )
    }
}

private data class RadioDrawable(val inner: Drawable) : Drawable {
    companion object {
        private const val FACTOR = 1.4142135f
    }

    override val size = (inner.size.toSize() * FACTOR).ceilToIntSize()
    private val translateOffset = (inner.size.height / FACTOR).roundToInt()
    override val padding = IntPadding(
        left = ceil(inner.padding.left * FACTOR).toInt(),
        top = ceil(inner.padding.top * FACTOR).toInt(),
        right = ceil(inner.padding.right * FACTOR).toInt(),
        bottom = ceil(inner.padding.bottom * FACTOR).toInt(),
    )

    override fun draw(
        canvas: Canvas,
        dstRect: IntRect,
        tint: Color,
    ) {
        canvas.pushState()
        canvas.translate(translateOffset, 0)
        canvas.rotate(45f)
        inner.draw(
            canvas = canvas,
            dstRect = IntRect(
                offset = dstRect.offset,
                size = (dstRect.size.toSize() / FACTOR).toIntSize(),
            ),
            tint = tint,
        )
        canvas.popState()
    }
}

private val disabledButtonTextColor = Color(0xFF484848u)

val OreUITheme = run {
    Theme(
        drawables = Theme.Drawables(
            button = DrawableSet(
                normal = OreUITextures.widget_button_button,
                focus = OutlineDrawable(OreUITextures.widget_button_button),
                hover = OreUITextures.widget_button_button_hover,
                active = OreUITextures.widget_button_button_active,
                disabled = OreUITextures.widget_button_button_disabled,
            ),
            guideButton = DrawableSet(
                normal = OreUITextures.widget_guide_button_guide_button,
                focus = OutlineDrawable(OreUITextures.widget_guide_button_guide_button),
                hover = OreUITextures.widget_guide_button_guide_button_hover,
                active = OreUITextures.widget_guide_button_guide_button_active,
                disabled = OreUITextures.widget_guide_button_guide_button_disabled,
            ),
            warningButton = DrawableSet(
                normal = OreUITextures.widget_warning_button_warning_button,
                focus = OutlineDrawable(OreUITextures.widget_warning_button_warning_button),
                hover = OreUITextures.widget_warning_button_warning_button_hover,
                active = OreUITextures.widget_warning_button_warning_button_active,
                disabled = OreUITextures.widget_warning_button_warning_button_disabled,
            ),

            textButton = DrawableSet(
                normal = Drawable.Empty,
                focus = OutlineDrawable(Drawable.Empty),
                hover = ColorDrawable(Color(0x55FFFFFFu)),
                active = ColorDrawable(Color(0xFF228207u)),
                disabled = ColorDrawable(Color(0xFF323335u)),
            ),

            checkbox = OnOffDrawableSet(
                off = DrawableSet(
                    normal = OreUITextures.widget_checkbox_checkbox_off,
                    focus = OutlineDrawable(OreUITextures.widget_checkbox_checkbox_off),
                    hover = OreUITextures.widget_checkbox_checkbox_off_hover,
                    active = OreUITextures.widget_checkbox_checkbox_off_active,
                    disabled = OreUITextures.widget_checkbox_checkbox_off_disabled,
                ),
                on = DrawableSet(
                    normal = OreUITextures.widget_checkbox_checkbox_on,
                    focus = OutlineDrawable(OreUITextures.widget_checkbox_checkbox_on),
                    hover = OreUITextures.widget_checkbox_checkbox_on_hover,
                    active = OreUITextures.widget_checkbox_checkbox_on_active,
                    disabled = OreUITextures.widget_checkbox_checkbox_on_disabled,
                ),
            ),

            radio = OnOffDrawableSet(
                off = DrawableSet(
                    normal = RadioDrawable(OreUITextures.widget_radio_radio_off),
                    focus = RadioDrawable(OutlineDrawable(OreUITextures.widget_radio_radio_off)),
                    hover = RadioDrawable(OreUITextures.widget_radio_radio_off_hover),
                    active = RadioDrawable(OreUITextures.widget_radio_radio_off_active),
                    disabled = RadioDrawable(OreUITextures.widget_radio_radio_off_disabled),
                ),
                on = DrawableSet(
                    normal = RadioDrawable(OreUITextures.widget_radio_radio_on),
                    focus = RadioDrawable(OutlineDrawable(OreUITextures.widget_radio_radio_on)),
                    hover = RadioDrawable(OreUITextures.widget_radio_radio_on_hover),
                    active = RadioDrawable(OreUITextures.widget_radio_radio_on_active),
                    disabled = RadioDrawable(OreUITextures.widget_radio_radio_on_disabled),
                ),
            ),

            sliderActiveTrack = DrawableSet(
                normal = OreUITextures.widget_slider_slider_active,
                disabled = OreUITextures.widget_slider_slider_active_disabled,
            ),
            sliderInactiveTrack = DrawableSet(
                normal = OreUITextures.widget_slider_slider_inactive,
                disabled = OreUITextures.widget_slider_slider_inactive_disabled,
            ),
            sliderHandle = DrawableSet(
                normal = OreUITextures.widget_handle_handle,
                focus = OutlineDrawable(OreUITextures.widget_handle_handle),
                hover = OreUITextures.widget_handle_handle_hover,
                active = OreUITextures.widget_handle_handle_active,
                disabled = OreUITextures.widget_handle_handle_disabled,
            ),

            switchFrame = DrawableSet(
                normal = OreUITextures.widget_switch_frame,
                disabled = OreUITextures.widget_switch_frame_disabled,
            ),
            switchBackground = TextureSet(
                normal = OreUITextures.widget_switch_switch,
                disabled = OreUITextures.widget_switch_switch_disabled,
            ),
            switchHandle = DrawableSet(
                normal = OreUITextures.widget_handle_handle,
                focus = OutlineDrawable(OreUITextures.widget_handle_handle),
                hover = OreUITextures.widget_handle_handle_hover,
                active = OreUITextures.widget_handle_handle_active,
                disabled = OreUITextures.widget_handle_handle_disabled,
            ),

            editText = DrawableSet(
                normal = OreUITextures.widget_textfield_textfield,
                focus = OutlineDrawable(OreUITextures.widget_textfield_textfield),
                disabled = OreUITextures.widget_textfield_textfield_disabled,
            ),

            selectMenuBox = DrawableSet(
                normal = OreUITextures.widget_select_select,
                focus = OutlineDrawable(OreUITextures.widget_select_select),
                hover = OreUITextures.widget_select_select_hover,
                active = OreUITextures.widget_select_select_active,
                disabled = OreUITextures.widget_select_select_disabled,
            ),

            itemGridBackground = OreUITextures.background_backpack,
        ),
        colors = Theme.Colors(
            button = ColorThemeSet(
                normal = ColorTheme.light.copy(
                    foreground = Color(0xFF1E1E1Eu),
                ),
                disabled = ColorTheme.light.copy(
                    foreground = disabledButtonTextColor,
                ),
            ),
            guideButton = ColorThemeSet(
                normal = ColorTheme.dark,
                disabled = ColorTheme.dark.copy(
                    foreground = disabledButtonTextColor,
                ),
            ),
            warningButton = ColorThemeSet(
                normal = ColorTheme.dark,
                disabled = ColorTheme.dark.copy(
                    foreground = disabledButtonTextColor,
                ),
            ),
        ),
    )
}
