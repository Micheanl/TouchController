package top.fifthlight.combine.theme.blackstone

import top.fifthlight.combine.core.paint.Color
import top.fifthlight.combine.core.paint.ColorDrawable
import top.fifthlight.combine.core.paint.Colors
import top.fifthlight.combine.core.paint.Drawable
import top.fifthlight.combine.theme.Theme
import top.fifthlight.combine.ui.style.*

val BlackstoneTheme = run {
    Theme(
        drawables = Theme.Drawables(
            button = DrawableSet(
                normal = BlackstoneTextures.widget_button_button,
                hover = BlackstoneTextures.widget_button_button_hover,
                active = BlackstoneTextures.widget_button_button_active,
                disabled = BlackstoneTextures.widget_button_button_disabled,
            ),
            guideButton = DrawableSet(
                normal = BlackstoneTextures.widget_guide_button_guide_button,
                hover = BlackstoneTextures.widget_guide_button_guide_button_hover,
                active = BlackstoneTextures.widget_guide_button_guide_button_active,
                disabled = BlackstoneTextures.widget_guide_button_guide_button_disabled,
            ),
            warningButton = DrawableSet(
                normal = BlackstoneTextures.widget_warning_button_warning_button,
                hover = BlackstoneTextures.widget_warning_button_warning_button_hover,
                active = BlackstoneTextures.widget_warning_button_warning_button_active,
                disabled = BlackstoneTextures.widget_warning_button_warning_button_disabled,
            ),

            textButton = DrawableSet(
                normal = Drawable.Empty,
                focus = ColorDrawable(Color(0x55FFFFFFu)),
                hover = ColorDrawable(Color(0x55FFFFFFu)),
                active = ColorDrawable(Color(0xFF228207u)),
                disabled = ColorDrawable(Color(0xFF323335u)),
            ),

            checkbox = OnOffDrawableSet(
                off = DrawableSet(
                    normal = BlackstoneTextures.widget_checkbox_checkbox_off,
                    hover = BlackstoneTextures.widget_checkbox_checkbox_off_hover,
                    active = BlackstoneTextures.widget_checkbox_checkbox_off_active,
                    disabled = BlackstoneTextures.widget_checkbox_checkbox_off_disabled,
                ),
                on = DrawableSet(
                    normal = BlackstoneTextures.widget_checkbox_checkbox_on,
                    hover = BlackstoneTextures.widget_checkbox_checkbox_on_hover,
                    active = BlackstoneTextures.widget_checkbox_checkbox_on_active,
                    disabled = BlackstoneTextures.widget_checkbox_checkbox_on_disabled,
                ),
            ),

            radio = OnOffDrawableSet(
                off = DrawableSet(
                    normal = BlackstoneTextures.widget_radio_radio_off,
                    hover = BlackstoneTextures.widget_radio_radio_off_hover,
                    active = BlackstoneTextures.widget_radio_radio_off_active,
                    disabled = BlackstoneTextures.widget_radio_radio_off_disabled,
                ),
                on = DrawableSet(
                    normal = BlackstoneTextures.widget_radio_radio_on,
                    hover = BlackstoneTextures.widget_radio_radio_on_hover,
                    active = BlackstoneTextures.widget_radio_radio_on_active,
                    disabled = BlackstoneTextures.widget_radio_radio_on_disabled,
                ),
            ),

            colorPickerHandleChoice = BlackstoneTextures.widget_color_picker_handle_choice,
            colorPickerSliderHandleHollow = DrawableSet(
                normal = BlackstoneTextures.widget_color_picker_hollow_handle,
                hover = BlackstoneTextures.widget_color_picker_hollow_handle_hover,
                active = BlackstoneTextures.widget_color_picker_hollow_handle_active,
                disabled = BlackstoneTextures.widget_color_picker_hollow_handle_disabled,
            ),

            sliderActiveTrack = DrawableSet(
                normal = BlackstoneTextures.widget_slider_slider_active,
                hover = BlackstoneTextures.widget_slider_slider_active_hover,
                active = BlackstoneTextures.widget_slider_slider_active_active,
                disabled = BlackstoneTextures.widget_slider_slider_active_disabled,
            ),
            sliderInactiveTrack = DrawableSet(
                normal = BlackstoneTextures.widget_slider_slider_inactive,
                hover = BlackstoneTextures.widget_slider_slider_inactive_hover,
                active = BlackstoneTextures.widget_slider_slider_inactive_active,
                disabled = BlackstoneTextures.widget_slider_slider_inactive_disabled,
            ),
            sliderHandle = DrawableSet(
                normal = BlackstoneTextures.widget_handle_handle,
                hover = BlackstoneTextures.widget_handle_handle_hover,
                active = BlackstoneTextures.widget_handle_handle_active,
                disabled = BlackstoneTextures.widget_handle_handle_disabled,
            ),

            switchFrame = DrawableSet(
                normal = BlackstoneTextures.widget_switch_frame,
                hover = BlackstoneTextures.widget_switch_frame_hover,
                active = BlackstoneTextures.widget_switch_frame_active,
                disabled = BlackstoneTextures.widget_switch_frame_disabled,
            ),
            switchBackground = TextureSet(
                normal = BlackstoneTextures.widget_switch_switch,
                hover = BlackstoneTextures.widget_switch_switch_hover,
                active = BlackstoneTextures.widget_switch_switch_active,
                disabled = BlackstoneTextures.widget_switch_switch_disabled,
            ),
            switchHandle = DrawableSet(
                normal = BlackstoneTextures.widget_handle_handle,
                hover = BlackstoneTextures.widget_handle_handle_hover,
                active = BlackstoneTextures.widget_handle_handle_active,
                disabled = BlackstoneTextures.widget_handle_handle_disabled,
            ),

            editText = DrawableSet(
                normal = BlackstoneTextures.widget_textfield_textfield,
                hover = BlackstoneTextures.widget_textfield_textfield_hover,
                active = BlackstoneTextures.widget_textfield_textfield_active,
                disabled = BlackstoneTextures.widget_textfield_textfield_disabled,
            ),

            iconButton = DrawableSet(
                normal = BlackstoneTextures.widget_icon_button_icon_button_off,
                hover = BlackstoneTextures.widget_icon_button_icon_button_off_hover,
                active = BlackstoneTextures.widget_icon_button_icon_button_off_active,
                disabled = BlackstoneTextures.widget_icon_button_icon_button_off_disabled,
            ),
            selectedIconButton = DrawableSet(
                normal = BlackstoneTextures.widget_icon_button_icon_button_on,
                hover = BlackstoneTextures.widget_icon_button_icon_button_on_hover,
                active = BlackstoneTextures.widget_icon_button_icon_button_on_active,
                disabled = BlackstoneTextures.widget_icon_button_icon_button_on_disabled,
            ),

            selectMenuBox = DrawableSet(
                normal = BlackstoneTextures.widget_select_select,
                hover = BlackstoneTextures.widget_select_select_hover,
                active = BlackstoneTextures.widget_select_select_active,
                disabled = BlackstoneTextures.widget_select_select_disabled,
            ),

            selectFloatPanel = BlackstoneTextures.widget_background_float_window,
            selectItem = OnOffDrawableSet(
                off = DrawableSet(
                    normal = BlackstoneTextures.widget_list_button_list_button_off,
                    hover = BlackstoneTextures.widget_list_button_list_button_off_hover,
                    active = BlackstoneTextures.widget_list_button_list_button_off_active,
                    disabled = BlackstoneTextures.widget_list_button_list_button_off_disabled,
                ),
                on = DrawableSet(
                    normal = BlackstoneTextures.widget_list_button_list_button_on,
                    hover = BlackstoneTextures.widget_list_button_list_button_on_hover,
                    active = BlackstoneTextures.widget_list_button_list_button_on_active,
                    disabled = BlackstoneTextures.widget_list_button_list_button_on_disabled,
                ),
            ),
            selectIconUp = BlackstoneTextures.icon_up,
            selectIconDown = BlackstoneTextures.icon_down,

            itemGridBackground = BlackstoneTextures.background_backpack,
        ),
        colors = Theme.Colors(
            button = ColorThemeSet(
                normal = ColorTheme.light,
                disabled = ColorTheme.light.copy(foreground = Colors.SECONDARY_WHITE),
            ),
            guideButton = ColorThemeSet(
                normal = ColorTheme.dark,
                disabled = ColorTheme.dark.copy(foreground = Colors.SECONDARY_WHITE),
            ),
            warningButton = ColorThemeSet(
                normal = ColorTheme.dark,
                disabled = ColorTheme.dark.copy(foreground = Colors.SECONDARY_WHITE),
            ),

            tabButton = OnOffColorSet(
                off = ColorThemeSet(
                    normal = ColorTheme.dark,
                    disabled = ColorTheme.dark.copy(foreground = Colors.SECONDARY_WHITE),
                ),
                on = ColorThemeSet(
                    normal = ColorTheme.light,
                    disabled = ColorTheme.light.copy(foreground = Colors.SECONDARY_WHITE),
                ),
            ),

            textButton = ColorThemeSet(
                normal = ColorTheme.dark,
                disabled = ColorTheme.dark.copy(foreground = Colors.SECONDARY_WHITE),
            ),
        ),
    )
}
