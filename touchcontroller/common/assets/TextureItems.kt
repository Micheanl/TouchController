/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.assets

import top.fifthlight.combine.paint.Texture
import top.fifthlight.mergetools.api.ExpectFactory
import top.fifthlight.touchcontroller.common.util.registry.SimpleRegistry

object TextureItems {
    val registry = SimpleRegistry<TextureItem>()

    fun register(
        id: String,
        name: String,
        hidden: Boolean = false,
        get: (TextureSet) -> Texture,
    ): TextureItem = TextureItem(
        name = name,
        hidden = hidden,
        get = get,
    ).also {
        registry.register(id, it)
    }

    val unknown = register(
        id = "unknown",
        name = "Unknown",
        hidden = true,
        get = { Texture.empty },
    )

    init {
        BuiltInTextureItemsInitializerFactory.of().register()
    }
}

interface BuiltInTextureItemsInitializer {
    fun register()

    @ExpectFactory
    interface Factory {
        fun of(): BuiltInTextureItemsInitializer
    }
}
