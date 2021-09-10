package dev.fritz2.tailwind.hooks

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.Option
import dev.fritz2.dom.html.Select
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class OptionsHook<T, C : Tag<*>, O : Tag<*>> {
    var options: List<T>? = null
    abstract var renderOptionLabel: O.(T) -> Unit

    // oder inline val
    abstract val apply: C.(Flow<T>) -> Unit

    operator fun invoke(options: List<T>, renderOptionLabel: O.(T) -> Unit = this.renderOptionLabel) {
        this.options = options
        this.renderOptionLabel = renderOptionLabel
    }

    operator fun invoke(vararg options: T, renderOptionLabel: O.(T) -> Unit = this.renderOptionLabel) {
        this.options = options.toList()
        this.renderOptionLabel = renderOptionLabel
    }
}

fun <T, C : Tag<*>, O : Tag<*>> C.hook(h: OptionsHook<T, C, O>, data: Flow<T>) = h.apply.invoke(this, data)


/*
 * TODO: overwritten vals or inline vals in constructor?
 */
class SelectOptionsHook<T> : OptionsHook<T, Select, Option>() {
    override var renderOptionLabel: Option.(T) -> Unit = { opt ->
        +opt.toString()
    }

    override val apply: Select.(Flow<T>) -> Unit = { data ->
        options?.forEach { opt ->
            option {
                renderOptionLabel(opt)
                selected(data.map { it == opt })
            }
        }
    }
}
