/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.assets

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import top.fifthlight.combine.paint.Texture

@Serializable(with = TextureItemSerializer::class)
data class TextureItem(
    val name: String,
    val hidden: Boolean = false,
    val get: (TextureSet) -> Texture,
)

class TextureItemSerializer : KSerializer<TextureItem> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "top.fifthlight.touchcontroller.common.assets.TextureItem",
        kind = PrimitiveKind.STRING,
    )

    override fun serialize(encoder: Encoder, value: TextureItem) = encoder.encodeString(
        TextureItems.registry.getId(value)
            ?: throw SerializationException("TextureItem $value not registered")
    )

    override fun deserialize(decoder: Decoder): TextureItem =
        TextureItems.registry[decoder.decodeString()] ?: TextureItems.unknown
}
