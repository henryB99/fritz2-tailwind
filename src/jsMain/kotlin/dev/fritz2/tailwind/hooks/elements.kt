package dev.fritz2.tailwind.hooks

import dev.fritz2.dom.Tag
import kotlinx.coroutines.flow.Flow


class AttributeHook<T, E : Tag<*>>(
    private val valueSetter: E.(T) -> Unit,
    private val flowOfValueSetter: E.(Flow<T>) -> Unit
) : SimpleHook<E>() {

    operator fun invoke(value: T) {
        apply = { valueSetter(value) }
    }

    operator fun invoke(value: Flow<T>) {
        apply = { flowOfValueSetter(value) }
    }
}

class BooleanAttributeHook<Boolean, E : Tag<*>>(
    private val valueSetter: E.(Boolean, String) -> Unit,
    private val flowOfValueSetter: E.(Flow<Boolean>, String) -> Unit,
    private val trueValue: String = ""
) : SimpleHook<E>() {

    operator fun invoke(value: Boolean) {
        apply = { valueSetter(value, trueValue) }
    }

    operator fun invoke(value: Flow<Boolean>) {
        apply = { flowOfValueSetter(value, trueValue) }
    }
}

class RawAttributeHook<T, E : Tag<*>>(private val name: String) : SimpleHook<E>() {
    operator fun invoke(value: T) {
        apply = { attr(name, value) }
    }

    operator fun invoke(value: Flow<T>) {
        apply = { attr(name, value) }
    }

    operator fun invoke(value: Boolean, trueValue: String = "") {
        apply = { attr(name, value, trueValue) }
    }

    operator fun invoke(value: Flow<Boolean>, trueValue: String = "") {
        apply = { attr(name, value, trueValue) }
    }

    operator fun invoke(values: List<String>, separator: String = " ") {
        apply = { attr(name, values, separator) }
    }

    operator fun invoke(values: Flow<List<String>>, separator: String = " ") {
        apply = { attr(name, values, separator) }
    }

    operator fun invoke(values: Map<String, Boolean>, separator: String = " ") {
        apply = { attr(name, values, separator) }
    }

    operator fun invoke(values: Flow<Map<String, Boolean>>, separator: String = " ") {
        apply = { attr(name, values, separator) }
    }
}
