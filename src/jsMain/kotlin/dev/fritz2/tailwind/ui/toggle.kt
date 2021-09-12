package dev.fritz2.tailwind.ui

import dev.fritz2.dom.html.Button
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.hooks.Initializer
import dev.fritz2.tailwind.hooks.TextHook
import dev.fritz2.tailwind.hooks.ToggleDatabindingHook
import dev.fritz2.tailwind.hooks.hook
import kotlinx.coroutines.flow.map


class Toggle(initializer: Initializer<Toggle>) : Component<Button> {

    val label = TextHook()
    val value = ToggleDatabindingHook()

    override fun RenderContext.render(classes: String?, id: String?) =
        button(
            "relative inline-flex flex-shrink-0 h-6 w-11 border-2 border-transparent rounded-full cursor-pointer transition-colors ease-in-out duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 $classes",
            id = id ?: value.id // ?: generateId if necessary
        ) {
            className(value.data.map { if (it) "bg-indigo-600" else "bg-gray-200" })
            type("button")
            span("sr-only") { hook(label) }
            span(" pointer-events-none inline-block h-5 w-5 rounded-full bg-white shadow transform ring-0 transition ease-in-out duration-200") {
                /* <!-- Enabled: "translate-x-5", Not Enabled: "translate-x-0" --> */
                className(value.data.map { if (it) "translate-x-5" else "translate-x-0" })
                attr("aria-hidden", "true")
            }
            hook(value)
        }

    init {
        initializer()
    }
}

fun RenderContext.toggle(
    classes: String? = null,
    id: String? = null,
    init: Initializer<Toggle>
): Button = Toggle(init).run { render(classes, id) }
