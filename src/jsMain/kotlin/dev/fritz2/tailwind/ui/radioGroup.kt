package dev.fritz2.tailwind.ui

import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.FieldSet
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.values
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.hooks.*
import kotlinx.coroutines.flow.map


class RadioGroup<T>(initializer: Initializer<RadioGroup<T>>) : Component<Div> {

    val label = TextHook()
    val options = RadioOptionsHook<T>()
    val value = DatabindingHook<T, Int, FieldSet>(
        /*
         * get rid of action and handler
         * forward handler-lamda from invoke to applyData
         * use it in options (add parameter to hook)
         * register changes to options mapping them (filter?) to T and combining them to one flow
         */
        action = { changes.values().map { it.toInt() } },
        handler = { handle { old, index -> options.options?.get(index) ?: old } },
        applyData = { data ->
            hook(options, data)
        }
    )

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
