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
import hook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class CheckboxGroupOptionsHook<T> : OptionsHook<T, List<T>, FieldSet, Div>() {
    //TODO: add Flow<Boolean> as second parameter
    override var renderOptionLabel: Div.(T) -> Unit = { opt ->
        label("font-medium text-gray-700") {
            //FIMXE: id?
//            `for`("comments")
            +opt.toString()
        }
//        p(
//            id = "comments-description",
//            baseClass = "text-gray-500"
//        ) { +"""Get notified when someones posts a comment on a posting.""" }
    }

    override val apply: FieldSet.(Flow<List<T>>, FieldSet.(Flow<List<T>>) -> Unit) -> Unit = { data, handle ->
        val fs = this //FIXME: no better way to do so???
        options?.withIndex()?.forEach { (index, opt) ->
            val checkedFlow = data.map { it.contains(opt) }

            div("relative flex items-start") {
                div("flex items-center h-5") {
                    input(
                        "focus:ring-indigo-500 h-4 w-4 text-indigo-600 border-gray-300 rounded"
                    ) {
                        //TODO: where to get this from?
//                        attr("aria-describedby", "comments-description")
//                        attr("aria-describedby", "comments-description")
                        checked(checkedFlow)
                        type("checkbox")
                        fs.handle(data.flatMapLatest { value ->
                            changes.states().map {
                                if (it) value + opt
                                else value - opt
                            }
                        })
                    }
                }
                div("ml-3 text-sm") {
                    renderOptionLabel(opt)
                }
            }
        }
    }
}


class CheckboxGroup<T>(initializer: Initializer<CheckboxGroup<T>>) : Component<Div> {

    val label = TextHook()
    val options = CheckboxGroupOptionsHook<T>()
    val value = OptionsDelagtingDatabindingHook(options)

    override fun RenderContext.render(classes: String?, id: String?) = div(classes) {
        if (label.isSet) {
            label("block text-sm font-medium text-gray-700") {
                `for`("location")
                hook(label)
            }
        }
        val indentCheckboxes = if (label.isSet) "mx-4" else ""
        fieldset("space-y-5 $indentCheckboxes") {
            legend("sr-only") { hook(label) }
            hook(value)
        }
    }

    init {
        initializer()
    }
}

fun <T> RenderContext.checkboxGroup(
    classes: String? = null,
    id: String? = null,
    init: Initializer<CheckboxGroup<T>>
): Div = CheckboxGroup(init).run { render(classes, id) }
