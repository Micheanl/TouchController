/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.ui.layer.tab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import cafe.adriel.voyager.core.screen.Screen
import top.fifthlight.combine.core.data.Identifier
import top.fifthlight.touchcontroller.common.config.condition.LayerConditions
import top.fifthlight.touchcontroller.common.config.preset.LayoutPreset
import top.fifthlight.touchcontroller.common.config.preset.info.LayerCustomConditions

data class LayerConditionTabContext(
    val preset: LayoutPreset?,
    val onCustomConditionsChanged: (LayerCustomConditions) -> Unit,
    val onConditionAdded: (LayerConditions.Key) -> Unit,
)

val LocalLayerConditionTabContext =
    compositionLocalOf<LayerConditionTabContext> { error("No LayerConditionTabContext") }

abstract class LayerConditionTab : Screen {
    @Composable
    abstract fun Icon()

    open val needBorder: Boolean
        get() = true

    abstract val name: Identifier
}

