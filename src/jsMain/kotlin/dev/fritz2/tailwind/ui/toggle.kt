package dev.fritz2.tailwind.ui

import dev.fritz2.binding.Store
import dev.fritz2.dom.html.Button
import dev.fritz2.dom.html.RenderContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


fun RenderContext.toggle(
    classes: String? = null,
    label: String,
    id: String? = null,
    store: Store<Boolean>
): Button =
    toggle(classes, label = label, id = id, value = store.data) {
        clicks handledBy store.handle { !it }
    }


fun RenderContext.toggle(
    classes: String? = null,
    label: String,
    id: String? = null,
    value: Flow<Boolean>,
    init: Button.() -> Unit
): Button =
    button(
        "bg-gray-200 relative inline-flex flex-shrink-0 h-6 w-11 border-2 border-transparent rounded-full cursor-pointer transition-colors ease-in-out duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 $classes",
        id = id
    ) {
        type("button")
        attr("role", "switch")
        attr("aria-checked", value, trueValue = "true")
        span("sr-only") { +label }
        span(" pointer-events-none inline-block h-5 w-5 rounded-full bg-white shadow transform ring-0 transition ease-in-out duration-200") {
            /* <!-- Enabled: "translate-x-5", Not Enabled: "translate-x-0" --> */
            className(value.map { if (it) "translate-x-5" else "translate-x-0" })
            attr("aria-hidden", "true")
        }
        init()
    }