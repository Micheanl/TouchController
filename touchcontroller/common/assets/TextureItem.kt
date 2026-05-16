/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.assets

import kotlinx.serialization.Serializable
import top.fifthlight.combine.core.paint.Texture
import top.fifthlight.touchcontroller.common.util.registry.RegistrySerializer

@Serializable(with = TextureItemSerializer::class)
data class TextureItem(
    val name: String,
    val hidden: Boolean = false,
    val get: (TextureSet) -> Texture,
)

class TextureItemSerializer : RegistrySerializer<TextureItem>(
    registry = TextureItems.registry,
    serialName = "TextureItem",
    unknown = { TextureItems.unknown },
)
