/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.config.data

import kotlinx.serialization.Serializable
import top.fifthlight.combine.core.paint.Color
import top.fifthlight.combine.core.paint.Colors.WHITE

@Serializable
data class ChatConfig(
    val lineSpacing: Int = 0,
    val textColor: Color = WHITE,
)
