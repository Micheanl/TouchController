package top.fifthlight.touchcontroller.common.util.registry

interface Registry<T> {
    operator fun get(id: String): T?
    fun getId(value: T): String?
}

interface MutableRegistry<T> : Registry<T> {
    fun register(id: String, value: T): MutableRegistry<T>

    operator fun set(id: String, value: T) {
        register(id, value)
    }
}
