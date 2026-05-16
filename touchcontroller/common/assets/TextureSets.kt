/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.assets

import top.fifthlight.combine.core.data.Identifier
import top.fifthlight.combine.core.data.Text
import top.fifthlight.mergetools.api.ExpectFactory
import top.fifthlight.touchcontroller.common.util.registry.SimpleRegistry

object TextureSets {
    val registry = SimpleRegistry<TextureSet>()

    fun register(
        id: String,
        name: Identifier,
        title: Identifier,
        grayWhenActive: Boolean,
        classic: Boolean,
    ) = TextureSet(
        name = Text.translatable(name),
        title = Text.translatable(title),
        grayWhenActive = grayWhenActive,
        classic = classic,
    ).also { registry.register(id, it) }

    private var _fallback: TextureSet? = null
    val fallback: TextureSet by lazy {
        BuiltInTextureSetsInitializerFactory.of().register()
        _fallback ?: error("Fallback TextureSet not registered")
    }

    fun registerFallback(textureSet: TextureSet) {
        check(_fallback == null || textureSet == _fallback) { "There is already registered a fallback TextureSet" }
        _fallback = textureSet
    }
}

interface BuiltInTextureSetsInitializer {
    fun register()

    @ExpectFactory
    interface Factory {
        fun of(): BuiltInTextureSetsInitializer
    }
}
