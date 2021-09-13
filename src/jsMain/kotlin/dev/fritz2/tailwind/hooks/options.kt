package dev.fritz2.tailwind.hooks

import dev.fritz2.dom.Tag
import kotlinx.coroutines.flow.Flow

abstract class OptionsHook<T, E, C : Tag<*>, O : Tag<*>> {
    var options: List<T>? = null
    abstract var renderOptionLabel: O.(T) -> Unit

    //TODO: make fun
    abstract val apply: C.(Flow<E>, C.(Flow<E>) -> Unit) -> Unit

    //TODO: offer context here instead of o to provide specific dsl (comments, etc.)
    operator fun invoke(options: List<T>, renderOptionLabel: O.(T) -> Unit = this.renderOptionLabel) {
        this.options = options
        this.renderOptionLabel = renderOptionLabel
    }

    operator fun invoke(vararg options: T, renderOptionLabel: O.(T) -> Unit = this.renderOptionLabel) {
        this.options = options.toList()
        this.renderOptionLabel = renderOptionLabel
    }
}

fun <T, E, C : Tag<*>, O : Tag<*>> C.hook(h: OptionsHook<T, E, C, O>, data: Flow<E>, handle: C.(Flow<E>) -> Unit) =
    h.apply.invoke(this, data, handle)


class OptionsDelagtingDatabindingHook<T, E, C : Tag<*>, O : Tag<*>>(private val options: OptionsHook<T, E, C, O>) :
    DatabindingHook<E, C>() {
    override fun C.render(handle: C.(Flow<E>) -> Unit) {
        hook(options, data, handle)
    }
}
