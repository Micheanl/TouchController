/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.layout.widget

import top.fifthlight.combine.core.paint.Color
import top.fifthlight.data.IntOffset
import top.fifthlight.touchcontroller.common.layout.Context

fun Context.Color(color: Color) {
    drawQueue.enqueue { canvas ->
        canvas.fillRect(IntOffset.ZERO, size, color)
    }
}
