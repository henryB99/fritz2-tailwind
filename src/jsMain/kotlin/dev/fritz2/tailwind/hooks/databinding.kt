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

class ToggleDatabindingHook : DatabindingHook<Boolean, Tag<*>>() {
    override fun Tag<*>.render(handle: Tag<*>.(Flow<Boolean>) -> Unit) {
        attr("role", "switch")
        attr("aria-checked", data, trueValue = "true")

        handle(data.flatMapLatest { value ->
            clicks.map { !value }
        })
    }
}

// TODO: Check if still needed / useful
abstract class StoreInterceptingHook<T, Y : Tag<*>> : DatabindingHook<T, Y>() {
    var store: Store<T>? = null

    override operator fun invoke(store: Store<T>) {
        this.store = store
        super.invoke(store)
    }
}

// Following Concepts not final yet!
abstract class ValidationMessageInterceptingHook<T, Y : Tag<*>> : DatabindingHook<T, Y>() {
    var messages: Flow<ComponentValidationMessage?>? = null

    open operator fun invoke(
        id: String? = null,
        data: Flow<T>,
        messages: Flow<ComponentValidationMessage?>? = null,
        handler: Y.(Flow<T>) -> Unit // TODO: Check if nullable would make sense!
    ) {
        this.messages = messages
        super.invoke(id, data, handler)
    }

    override operator fun invoke(store: Store<T>) {
        messages = store.validationMessage()
        super.invoke(store)
    }
}

class InputDatabindingHook : ValidationMessageInterceptingHook<String, Input>() {

    override fun Input.render(handle: Input.(Flow<String>) -> Unit) {
        handle(changes.values())
        value(data)
    }
}

// Might not be an hook at all! -> probably no information at configuration time!!! ("void"-hook ist not really a hook)
//  Could therefore omit all type dependence!
class ValidationMessageHook<T, Y: Tag<*>>(private val interception: ValidationMessageInterceptingHook<T, Y>) :
    SimpleHook<T>() {

    /*
    override fun Input.render(handle: Input.(Flow<String>) -> Unit) {
        val messages = interception.messages
        TODO("Not yet implemented")
    }
     */
}