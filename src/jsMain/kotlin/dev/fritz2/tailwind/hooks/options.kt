package dev.fritz2.tailwind.hooks

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.FieldSet
import dev.fritz2.dom.html.Option
import dev.fritz2.dom.html.Select
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class OptionsHook<T, C : Tag<*>, O : Tag<*>> {
    var options: List<T>? = null
    abstract var renderOptionLabel: O.(T) -> Unit

    // oder inline val
    abstract val apply: C.(Flow<T>) -> Unit

    operator fun invoke(options: List<T>, renderOptionLabel: O.(T) -> Unit = this.renderOptionLabel) {
        this.options = options
        this.renderOptionLabel = renderOptionLabel
    }

    operator fun invoke(vararg options: T, renderOptionLabel: O.(T) -> Unit = this.renderOptionLabel) {
        this.options = options.toList()
        this.renderOptionLabel = renderOptionLabel
    }
}

fun <T, C : Tag<*>, O : Tag<*>> C.hook(h: OptionsHook<T, C, O>, data: Flow<T>) = h.apply.invoke(this, data)


/*
 * TODO: overwritten vals or inline vals in constructor?
 */
class SelectOptionsHook<T> : OptionsHook<T, Select, Option>() {
    override var renderOptionLabel: Option.(T) -> Unit = { opt ->
        +opt.toString()
    }

    override val apply: Select.(Flow<T>) -> Unit = { data ->
        options?.forEach { opt ->
            option {
                renderOptionLabel(opt)
                selected(data.map { it == opt })
            }
        }
    }
}

/*
 * TODO: overwritten vals or inline vals in constructor?
 */
class RadioOptionsHook<T> : OptionsHook<T, FieldSet, Div>() {
    //TODO: add Flow<Boolean> as second parameter
    override var renderOptionLabel: Div.(T) -> Unit = { opt ->
        /* <!-- Checked: "text-indigo-900", Not Checked: "text-gray-900" --> */
        span(
            id = "privacy-setting-0-label",
            baseClass = "block text-sm font-medium"
        ) { +opt.toString() }
        /* <!-- Checked: "text-indigo-700", Not Checked: "text-gray-500" --> */
//        span(
//            id = "privacy-setting-0-description",
//            baseClass = "block text-sm"
//        ) { +"""This project would be available to anyone who has the link""" }
    }

    override val apply: FieldSet.(Flow<T>) -> Unit = { data ->
        div("bg-white rounded-md -space-y-px") {
            options?.withIndex()?.forEach { (index, opt) ->
                val checkedFlow = data.map { it == opt }
                label("rounded-tl-md rounded-tr-md relative border p-4 flex cursor-pointer focus:outline-none") {
                    /* <!-- Checked: "bg-indigo-50 border-indigo-200 z-10", Not Checked: "border-gray-200" --> */
                    className(checkedFlow.map { if (it) "bg-indigo-50 border-indigo-200 z-10" else "border-gray-200" })

                    input("h-4 w-4 mt-0.5 cursor-pointer text-indigo-600 border-gray-300 focus:ring-indigo-500") {
                        type("radio")
                        name("privacy-setting")
                        checked(checkedFlow)
                        value(index.toString())
                        attr("aria-labelledby", "privacy-setting-0-label")
                        attr("aria-describedby", "privacy-setting-0-description")
                    }
                    div("ml-3 flex flex-col") {
                        renderOptionLabel(opt)
                    }
                }
            }
        }
    }
}