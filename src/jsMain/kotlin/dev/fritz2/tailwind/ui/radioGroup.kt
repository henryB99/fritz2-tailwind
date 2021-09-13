package dev.fritz2.tailwind.ui

import TextHook
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.FieldSet
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.hooks.DatabindingHook
import dev.fritz2.tailwind.hooks.Initializer
import dev.fritz2.tailwind.hooks.RadioGroupOptionsHook
import dev.fritz2.tailwind.hooks.hook
import kotlinx.coroutines.flow.Flow


class RadioGroupDatabindingHook<T>(private val options: RadioGroupOptionsHook<T>) : DatabindingHook<T, FieldSet>() {
    override fun FieldSet.render(handle: FieldSet.(Flow<T>) -> Unit) {
        hook(options, data, handle)
    }
}

class RadioGroup<T>(initializer: Initializer<RadioGroup<T>>) : Component<Div> {

    // TODO: what to do with this one?
    val label = TextHook()
    val options = RadioGroupOptionsHook<T>()
    val value = RadioGroupDatabindingHook(options)

    override fun RenderContext.render(classes: String?, id: String?) = div(classes) {
        fieldset(id = id ?: value.id) {
            legend("sr-only") { hook(label) }
            hook(value)
        }
    }

    init {
        initializer()
    }
}

fun <T> RenderContext.radioGroup(
    classes: String? = null,
    id: String? = null,
    init: Initializer<RadioGroup<T>>
): Div = RadioGroup(init).run { render(classes, id) }
