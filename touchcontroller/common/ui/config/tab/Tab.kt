/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.ui.config.tab

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import top.fifthlight.combine.core.data.Identifier
import top.fifthlight.combine.core.data.Text
import top.fifthlight.touchcontroller.common.config.GlobalConfig

typealias OnResetHandler = GlobalConfig.() -> GlobalConfig

data class TabOptions(
    private val titleId: Identifier,
    val group: TabGroup? = null,
    val index: Int,
    val openAsScreen: Boolean = false,
    val onReset: OnResetHandler? = null,
) {
    val title: Text
        @Composable
        get() = Text.translatable(titleId)
}

abstract class Tab : Screen {
    abstract val options: TabOptions
}
