/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.util.color

import top.fifthlight.combine.core.paint.Color
import top.fifthlight.combine.core.paint.Colors

object ColorHelper {
    fun opacityTint(opacity: Float) = if (opacity == 1f) {
        Colors.WHITE
    } else {
        val realOpacity = opacity.coerceIn(0f..1f)
        Color(((0xFF * realOpacity).toInt() shl 24) or 0xFFFFFF)
    }

    fun mixOpacity(color: Color, opacity: Float) = if (opacity == 1f) {
        color
    } else {
        val realOpacity = color.aFloat * opacity.coerceIn(0f..1f)
        Color((color.value and 0x00FFFFFF) or ((0xFF * realOpacity).toInt() shl 24))
    }
}
