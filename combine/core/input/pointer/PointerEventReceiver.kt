package top.fifthlight.combine.core.input.pointer

fun interface PointerEventReceiver {
    fun onPointerEvent(event: PointerEvent): Boolean
}
