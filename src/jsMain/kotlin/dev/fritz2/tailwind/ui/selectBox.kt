package dev.fritz2.tailwind.ui

import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Select
import dev.fritz2.dom.selectedIndex
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.hooks.*


class SelectBox<T>(initializer: Initializer<SelectBox<T>>) : Component<Div> {

    val label = TextHook()
    val options = SelectOptionsHook<T>()
    val value = DatabindingHook<T, Int, Select>(
        action = { changes.selectedIndex() },
        handler = { handle<Int> { old, index -> options.options?.get(index) ?: old } },
        applyData = { data ->
            hook(options, data)
        }
    )

    override fun RenderContext.render(classes: String?, id: String?) = div(classes) {
        if (label.isSet) {
            label("block text-sm font-medium text-gray-700") {
                `for`("location")
                hook(label)
            }
        }
        select(
            baseClass = "mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md",
            id = id ?: value.id,
        ) {
            value.apply.invoke(this)
        }
    }

    init {
        initializer()
    }
}

fun <T> RenderContext.selectBox(
    classes: String? = null,
    id: String? = null,
    init: Initializer<SelectBox<T>>
): Div = SelectBox(init).run { render(classes, id) }
