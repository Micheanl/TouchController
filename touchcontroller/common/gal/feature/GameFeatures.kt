/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.gal.feature

import top.fifthlight.mergetools.api.ExpectFactory

data class GameFeatures(
    val dualWield: Boolean,
)

interface GameFeaturesProvider {
    val gameFeatures: GameFeatures

    @ExpectFactory
    interface Factory {
        fun of(): GameFeaturesProvider
    }
}
