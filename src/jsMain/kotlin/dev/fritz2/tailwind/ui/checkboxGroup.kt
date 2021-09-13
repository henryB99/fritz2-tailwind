package dev.fritz2.tailwind.ui

import TextHook
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.FieldSet
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.hooks.CheckboxGroupOptionsHook
import dev.fritz2.tailwind.hooks.DatabindingHook
import dev.fritz2.tailwind.hooks.Initializer
import dev.fritz2.tailwind.hooks.hook
import hook
import kotlinx.coroutines.flow.Flow


class CheckboxGroupDatabindingHook<T>(private val options: CheckboxGroupOptionsHook<T>) :
    DatabindingHook<List<T>, FieldSet>() {
    override fun FieldSet.render(handle: FieldSet.(Flow<List<T>>) -> Unit) {
        hook(options, data, handle)
    }
}

class CheckboxGroup<T>(initializer: Initializer<CheckboxGroup<T>>) : Component<Div> {

    val label = TextHook()
    val options = CheckboxGroupOptionsHook<T>()
    val value = CheckboxGroupDatabindingHook(options)

    override fun RenderContext.render(classes: String?, id: String?) = div(classes) {
        fieldset("space-y-5") {
            legend("sr-only") { hook(label) }
            hook(value)
        }
    }

    init {
        initializer()
    }
}

fun <T> RenderContext.checkboxGroup(
    classes: String? = null,
    id: String? = null,
    init: Initializer<CheckboxGroup<T>>
): Div = CheckboxGroup(init).run { render(classes, id) }
