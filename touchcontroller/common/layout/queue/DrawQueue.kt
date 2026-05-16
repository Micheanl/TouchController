/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.layout.queue

import top.fifthlight.combine.core.paint.Canvas

typealias DrawCall = (Canvas) -> Unit

class DrawQueue {
    private val items = mutableListOf<DrawCall>()

    fun enqueue(block: DrawCall) {
        items.add(block)
    }

    fun execute(canvas: Canvas) {
        items.forEach { it.invoke(canvas) }
    }
}
