package dev.fritz2.tailwind.ui.application.forms

import dev.fritz2.binding.Store
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.tailwind.ui.toggle


fun RenderContext.toggleField(
    classes: String? = null,
    label: String,
    id: String? = null,
    store: Store<Boolean>
): Div =
    div("flex items-center") {
        toggle(classes) {
            label(label)
            value(store)
        }
        span(baseClass = "ml-3", id = id?.let { "$id-label" }) {
            span("text-sm font-medium text-gray-900") { +label }
        }
    }
