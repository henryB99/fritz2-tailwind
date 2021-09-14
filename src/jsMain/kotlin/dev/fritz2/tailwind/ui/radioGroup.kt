package dev.fritz2.tailwind.ui

import TextHook
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.FieldSet
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.states
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.hooks.Initializer
import dev.fritz2.tailwind.hooks.OptionsDelagtingDatabindingHook
import dev.fritz2.tailwind.hooks.OptionsHook
import dev.fritz2.tailwind.hooks.hook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map


class RadioGroupOptionsHook<T> : OptionsHook<T, T, FieldSet, Div>() {
    //TODO: add Flow<Boolean> as second parameter
    override var renderOptionLabel: Div.(T) -> Unit = { opt ->
        span("block text-sm font-medium") {
            //className(checkedFlow.map { if(it) "text-indigo-900" else "text-gray-900"})
            +opt.toString()
        }
        /* <!-- Checked: "text-indigo-700", Not Checked: "text-gray-500" --> */
//        span(
//            id = "privacy-setting-0-description",
//            baseClass = "block text-sm"
//        ) { +"""This project would be available to anyone who has the link""" }
    }

    override val apply: FieldSet.(Flow<T>, FieldSet.(Flow<T>) -> Unit) -> Unit = { data, handle ->
        this.apply {
            div("bg-white rounded-md -space-y-px") {
                options?.withIndex()?.forEach { (index, opt) ->
                    val checkedFlow = data.map { it == opt }

                    val round = if (index == 0) " rounded-tl-md rounded-tr-md"
                    else if (index == (options?.size ?: 0) - 1) "rounded-bl-md rounded-br-md"
                    else ""

                    label("relative border p-4 flex cursor-pointer focus:outline-none $round") {
                        className(checkedFlow.map { if (it) "bg-indigo-50 border-indigo-200 z-10" else "border-gray-200" })

                        input("h-4 w-4 mt-0.5 cursor-pointer text-indigo-600 border-gray-300 focus:ring-indigo-500") {
                            type("radio")
                            //TODO: where to get this from?
                            name("privacy-setting")
                            checked(checkedFlow)
                            //TODO: How to handle this?
//                        attr("aria-labelledby", "privacy-setting-0-label")
//                        attr("aria-describedby", "privacy-setting-0-description")
                            this@apply.handle(changes.states().filter { true }.map { opt })
                        }
                        div("ml-3 flex flex-col") {
                            renderOptionLabel(opt)
                        }
                    }
                }
            }
        }
    }
}


class RadioGroup<T>(initializer: Initializer<RadioGroup<T>>) : Component<Div> {

    val label = TextHook()
    val options = RadioGroupOptionsHook<T>()
    val value = OptionsDelagtingDatabindingHook(options)

    override fun RenderContext.render(classes: String?, id: String?) = div(classes) {
        if (label.isSet) {
            label("block text-sm font-medium text-gray-700 mb-1") {
                `for`("location")
                hook(label)
            }
        }
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
