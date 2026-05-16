package top.fifthlight.combine.core.input.text

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import top.fifthlight.data.IntRect
import top.fifthlight.mergetools.api.ExpectFactory

interface InputHandler {
    val events: SharedFlow<TextInputState>
    fun updateInputState(textInputState: TextInputState?, cursorRect: IntRect? = null, areaRect: IntRect? = null)
    fun tryShowKeyboard()
    fun tryHideKeyboard()

    @ExpectFactory
    interface Factory {
        fun ofDefault(): InputHandler?
    }

    object Empty : InputHandler {
        override val events = MutableSharedFlow<TextInputState>()
            .asSharedFlow()

        override fun updateInputState(textInputState: TextInputState?, cursorRect: IntRect?, areaRect: IntRect?) {}
        override fun tryShowKeyboard() {}
        override fun tryHideKeyboard() {}
    }
}
