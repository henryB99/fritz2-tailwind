package dev.fritz2.tailwind.ui

import TextHook
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.Option
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Select
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.hooks.Initializer
import dev.fritz2.tailwind.hooks.OptionsDelagtingDatabindingHook
import dev.fritz2.tailwind.hooks.OptionsHook
import dev.fritz2.tailwind.hooks.hook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SelectOptionsHook<T> : OptionsHook<T, T, Select, Option>() {
    override var renderOptionLabel: Option.(T) -> Unit = { opt ->
        +opt.toString()
    }

    override val apply: Select.(Flow<T>, Select.(Flow<T>) -> Unit) -> Unit = { data, _ ->
        options?.forEach { opt ->
            option {
                renderOptionLabel(opt)
                selected(data.map { it == opt })
            }
        }
    }
}


class SelectBox<T>(initializer: Initializer<SelectBox<T>>) : Component<Div> {

    val label = TextHook()
    val options = SelectOptionsHook<T>()
    val value = OptionsDelagtingDatabindingHook(options)

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
