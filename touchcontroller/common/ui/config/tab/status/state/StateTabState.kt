/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.ui.config.tab.status.state

import top.fifthlight.combine.core.data.Text

data class StateTabState(
    val currentPlatform: Text? = null,
    val systemInfo: SystemInfo? = null,
    val warningMessage: Text? = null,
) {
    data class SystemInfo(
        val system: String,
        val arch: String,
    )
}
