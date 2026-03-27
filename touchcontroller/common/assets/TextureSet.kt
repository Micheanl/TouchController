/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.assets

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import top.fifthlight.combine.data.Text

@Serializable(with = TextureSetSerializer::class)
data class TextureSet(
    val name: Text,
    val title: Text,
    val grayWhenActive: Boolean,
    val classic: Boolean,
)

class TextureSetSerializer : KSerializer<TextureSet> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "top.fifthlight.touchcontroller.common.assets.TextureSet",
        kind = PrimitiveKind.STRING,
    )

    override fun serialize(encoder: Encoder, value: TextureSet) = encoder.encodeString(
        TextureSets.registry.getId(value)
            ?: throw SerializationException("TextureSet $value not registered")
    )

    override fun deserialize(decoder: Decoder): TextureSet =
        TextureSets.registry[decoder.decodeString()] ?: TextureSets.fallback
}
