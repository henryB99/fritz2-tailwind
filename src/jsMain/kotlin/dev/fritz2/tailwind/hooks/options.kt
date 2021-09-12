package dev.fritz2.tailwind.hooks

import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.FieldSet
import dev.fritz2.dom.html.Option
import dev.fritz2.dom.html.Select
import dev.fritz2.dom.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

abstract class OptionsHook<T, C : Tag<*>, O : Tag<*>> {
    var options: List<T>? = null
    abstract var renderOptionLabel: O.(T) -> Unit

    abstract val apply: C.(Flow<T>, C.(Flow<T>) -> Unit) -> Unit

    //TODO: offer context here instead of o to provide specific dsl (comments, etc.)
    operator fun invoke(options: List<T>, renderOptionLabel: O.(T) -> Unit = this.renderOptionLabel) {
        this.options = options
        this.renderOptionLabel = renderOptionLabel
    }

    operator fun invoke(vararg options: T, renderOptionLabel: O.(T) -> Unit = this.renderOptionLabel) {
        this.options = options.toList()
        this.renderOptionLabel = renderOptionLabel
    }
}

fun <T, C : Tag<*>, O : Tag<*>> C.hook(h: OptionsHook<T, C, O>, data: Flow<T>, handle: C.(Flow<T>) -> Unit) =
    h.apply.invoke(this, data, handle)


/*
 * TODO: overwritten vals or inline vals in constructor?
 */
class SelectOptionsHook<T> : OptionsHook<T, Select, Option>() {
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

/*
 * TODO: overwritten vals or inline vals in constructor?
 * TODO: Better here or next to component class
 */
class RadioGroupOptionsHook<T> : OptionsHook<T, FieldSet, Div>() {
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
        val fs = this //FIXME: no better way to do so???
        div("bg-white rounded-md -space-y-px") {
            options?.withIndex()?.forEach { (index, opt) ->
                val checkedFlow = data.map { it == opt }

                val round = if (index == 0) " rounded-tl-md rounded-tr-md"
                else if (index == (options?.size ?: 0) - 1) "rounded-bl-md rounded-br-md"
                else ""

                label("relative border p-4 flex cursor-pointer focus:outline-none $round") {
                    /* <!-- Checked: "bg-indigo-50 border-indigo-200 z-10", Not Checked: "border-gray-200" --> */
                    className(checkedFlow.map { if (it) "bg-indigo-50 border-indigo-200 z-10" else "border-gray-200" })

                    input("h-4 w-4 mt-0.5 cursor-pointer text-indigo-600 border-gray-300 focus:ring-indigo-500") {
                        type("radio")
                        name("privacy-setting")
                        checked(checkedFlow)
                        //TODO: How to handle this?
//                        attr("aria-labelledby", "privacy-setting-0-label")
//                        attr("aria-describedby", "privacy-setting-0-description")
                        fs.handle(changes.states().filter { true }.map { opt })
                    }
                    div("ml-3 flex flex-col") {
                        renderOptionLabel(opt)
                    }
                }
            }
        }
    }
}