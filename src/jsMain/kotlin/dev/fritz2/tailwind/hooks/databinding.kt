package dev.fritz2.tailwind.hooks

import dev.fritz2.binding.Store
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.values
import dev.fritz2.tailwind.validation.ComponentValidationMessage
import dev.fritz2.tailwind.validation.validationMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

abstract class DatabindingHook<T, Y : Tag<*>> : SimpleHook<Y>() {
    lateinit var data: Flow<T>
    var id: String? = null
    var messages: Flow<ComponentValidationMessage?>? = null

    abstract fun Y.render(handle: (Y.(Flow<T>) -> Unit)?)

    open operator fun invoke(
        id: String? = null,
        data: Flow<T>,
        messages: Flow<ComponentValidationMessage?>? = null,
        handler: (Y.(Flow<T>) -> Unit)? = null
    ) {
        this.id = id
        this.data = data
        this.messages = messages
        apply = {
            render(handler)
        }
    }

    open operator fun invoke(store: Store<T>) {
        messages = store.validationMessage()
        this.invoke(store.id, store.data) { it handledBy store.update }
    }
}

class ToggleDatabindingHook : DatabindingHook<Boolean, Tag<*>>() {
    override fun Tag<*>.render(handle: (Tag<*>.(Flow<Boolean>) -> Unit)?) {
        attr("role", "switch")
        attr("aria-checked", data, trueValue = "true")

        handle?.invoke(this, data.flatMapLatest { value ->
            clicks.map { !value }
        })
    }
}

class InputDatabindingHook : DatabindingHook<String, Input>() {
    override fun Input.render(handle: (Input.(Flow<String>) -> Unit)?) {
        handle?.invoke(this, changes.values())
        value(data)
    }
}
