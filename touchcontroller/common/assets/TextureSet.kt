/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.assets

import kotlinx.serialization.Serializable
import top.fifthlight.combine.core.data.Text
import top.fifthlight.touchcontroller.common.util.registry.RegistrySerializer

@Serializable(with = TextureSetSerializer::class)
data class TextureSet(
    val name: Text,
    val title: Text,
    val grayWhenActive: Boolean,
    val classic: Boolean,
)

class TextureSetSerializer : RegistrySerializer<TextureSet>(
    registry = TextureSets.registry,
    serialName = "TextureSet",
    unknown = { TextureSets.fallback },
)
