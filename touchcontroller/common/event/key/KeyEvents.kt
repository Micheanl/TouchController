/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2026 fifth_light
 */

package top.fifthlight.touchcontroller.common.event.key

import top.fifthlight.mergetools.api.ActualConstructor
import top.fifthlight.mergetools.api.ActualImpl
import top.fifthlight.touchcontroller.common.gal.key.KeyBindingEventsHandler
import top.fifthlight.touchcontroller.common.gal.key.KeyBindingState

@ActualImpl(KeyBindingEventsHandler::class)
object KeyEvents: KeyBindingEventsHandler {
    @JvmStatic
    @ActualConstructor
    fun of(): KeyBindingEventsHandler = this

    private val clickHandlers = mutableListOf<(KeyBindingState) -> Unit>()

    fun addClickHandler(handler: (KeyBindingState) -> Unit) {
        clickHandlers.add(handler)
    }

    override fun onKeyClicked(state: KeyBindingState) {
        clickHandlers.forEach {
            it.invoke(state)
        }
    }
}
