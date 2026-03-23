/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.util.registry

class SimpleRegistry<T> : MutableRegistry<T> {
    private val idToValue = mutableMapOf<String, T>()
    private val valueToId = mutableMapOf<T, String>()

    override fun register(
        id: String,
        value: T
    ): MutableRegistry<T> = also {
        if (id in idToValue) {
            throw IllegalArgumentException("ID $id is already registered")
        }
        idToValue[id] = value
    }

    override fun get(id: String): T? = idToValue[id]

    override fun getId(value: T): String? = valueToId[value]
}
