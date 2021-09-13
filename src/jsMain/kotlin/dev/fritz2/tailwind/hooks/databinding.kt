package dev.fritz2.tailwind.hooks

import dev.fritz2.binding.Store
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.values
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

abstract class DatabindingHook<T, Y : Tag<*>> : Hook<Y>() {
    lateinit var data: Flow<T>
    var id: String? = null

    abstract fun Y.render(handle: Y.(Flow<T>) -> Unit)

    open operator fun invoke(id: String? = null, data: Flow<T>, handler: Y.(Flow<T>) -> Unit) {
        this.id = id
        this.data = data
        apply = {
            render(handler)
        }
    }

    open operator fun invoke(store: Store<T>) {
        this.invoke(store.id, store.data) { it handledBy store.update }
    }
}


class InputDatabindingHook : DatabindingHook<String, Input>() {
    override fun Input.render(handle: Input.(Flow<String>) -> Unit) {
        handle(changes.values())
        value(data)
    }
}

class ToggleDatabindingHook : DatabindingHook<Boolean, Tag<*>>() {
    override fun Tag<*>.render(handle: Tag<*>.(Flow<Boolean>) -> Unit) {
        attr("role", "switch")
        attr("aria-checked", data, trueValue = "true")

        handle(data.flatMapLatest { value ->
            clicks.map { !value }
        })
    }
}
