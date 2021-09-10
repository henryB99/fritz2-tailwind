package dev.fritz2.tailwind.ui.hooks

import dev.fritz2.binding.Handler
import dev.fritz2.binding.Store
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.values
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.Element

open class DatabindingHook<T, X, Y : Tag<*>>(
    inline val action: Y.() -> Flow<X>,
    inline val handler: Store<T>.() -> Handler<X>,
    inline val applyData: Y.(Flow<T>) -> Unit
) {
    lateinit var data: Flow<T>
    lateinit var apply: Y.() -> Unit
    var id: String? = null

    open operator fun invoke(id: String? = null, data: Flow<T>, handler: Y.(Flow<X>) -> Unit) {
        this.id = id
        this.data = data
        apply = {
            applyData(data)
            handler(action())
        }
    }

    open operator fun invoke(store: Store<T>) {
        this.invoke(store.id, store.data) { it handledBy store.handler() }
    }
}

fun <T, E : Element, X, Y : Tag<E>> Y.hook(h: DatabindingHook<T, X, Y>) = h.apply.invoke(this)

class InputDatabindingHook : DatabindingHook<String, String, Input>(
    action = { changes.values() },
    handler = { update },
    applyData = { value(it) }
)

class ToggleDatabindingHook : DatabindingHook<Boolean, Unit, Tag<*>>(
    action = { clicks.events.map {} },
    handler = { handle { !it } },
    applyData = {
        attr("role", "switch")
        attr("aria-checked", it, trueValue = "true")
    }
)
