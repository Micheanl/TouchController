/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.platform

import top.fifthlight.combine.core.data.Text
import top.fifthlight.touchcontroller.proxy.message.ProxyMessage

interface Platform {
    val name: Text
    val useDefaultInputHandler: Boolean

    fun resize(width: Int, height: Int) {}
    fun pollEvent(): ProxyMessage?
    fun sendEvent(message: ProxyMessage)
}
