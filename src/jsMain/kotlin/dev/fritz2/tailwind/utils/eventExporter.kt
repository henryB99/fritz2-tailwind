import dev.fritz2.tailwind.hooks.Initializer

class Exporter<T : Any>(initializer: Initializer<Exporter<T>>) {
    lateinit var payload: T

    fun export(payload: T): T {
        this.payload = payload
        return payload
    }

    init {
        initializer()
    }
}

fun <T : Any> export(scope: Exporter<T>.() -> Unit): T =
    Exporter(scope).payload
