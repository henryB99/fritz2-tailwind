package dev.fritz2.tailwind.ui

import TextHook
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Select
import dev.fritz2.dom.selectedIndex
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.hooks.DatabindingHook
import dev.fritz2.tailwind.hooks.Initializer
import dev.fritz2.tailwind.hooks.SelectOptionsHook
import dev.fritz2.tailwind.hooks.hook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull


// TODO: as object at declaration? or is this performance overhead
class SelectDatabindingHook<T>(private val options: SelectOptionsHook<T>) : DatabindingHook<T, Select>() {
    override fun Select.render(handle: Select.(Flow<T>) -> Unit) {
        handle(changes.selectedIndex().mapNotNull { options.options?.get(it) })
        hook(options, data, handle)
    }
}

class SelectBox<T>(initializer: Initializer<SelectBox<T>>) : Component<Div> {

    val label = TextHook()
    val options = SelectOptionsHook<T>()
    val value = SelectDatabindingHook(options)

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
            hook(value)
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
