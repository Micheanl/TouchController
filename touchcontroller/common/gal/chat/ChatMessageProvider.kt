/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.gal.chat

import kotlinx.collections.immutable.PersistentList
import top.fifthlight.combine.core.data.Text
import top.fifthlight.mergetools.api.ExpectFactory

data class ChatMessage(
    val message: Text,
)

interface ChatMessageProvider {
    fun getMessages(): PersistentList<ChatMessage>
    fun sendMessage(message: String)

    @ExpectFactory
    interface Factory {
        fun of(): ChatMessageProvider
    }
}
