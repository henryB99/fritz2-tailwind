package dev.fritz2.tailwind.ui.application.forms

import dev.fritz2.binding.Store
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.values
import dev.fritz2.identification.uniqueId
import dev.fritz2.tailwind.ui.icon
import dev.fritz2.tailwind.ui.icons.Solid
import dev.fritz2.tailwind.validation.validationMessage
import kotlinx.coroutines.flow.map

internal fun RenderContext.renderHelpText(helpText: String?): Unit {
    helpText?.let {
        p(baseClass = "mt-2 text-sm text-gray-500", id = "$id-description") {
            +it
        }
    }
}

fun RenderContext.inputField(
    classes: String? = null,
    label: String? = null,
    type: String = "text",
    placeholder: String? = null,
    helpText: String? = null,
    describedBy: String? = null,
    trailing: (Div.() -> Unit)? = null,
    id: String = uniqueId(),
    init: (Input.() -> Unit)? = null,
    store: Store<String>? = null
): Div {
    val validationMsgs = store?.validationMessage()

    return div(classes) {
        label?.let {
            label("block text-sm font-medium text-gray-700") {
                `for`(id)
                +label
            }
        }

        //      val error = "pr-10 border-red-300 text-red-900 placeholder-red-300 focus:outline-none focus:ring-red-500 focus:border-red-500 sm:text-sm rounded-md"
        //    val good  = "focus:ring-indigo-500 focus:border-indigo-500 block w-full pr-10 sm:text-sm border-gray-300 rounded-md"

        div("mt-1 relative rounded-md shadow-sm") {
            input(
                "block w-full pr-10 sm:text-sm rounded-md",
                id = id
            ) {
                validationMsgs?.let {
                    className(it.map { msg ->
                        if (msg != null)
                            "border-red-300 text-red-900 placeholder-red-300 focus:outline-none focus:ring-red-500 focus:border-red-500"
                        else "focus:ring-indigo-500 focus:border-indigo-500 block border-gray-300"
                    })
                }
                type(type)
                placeholder?.let { placeholder(it) }
                describedBy?.let { attr("aria-describedby", it) }

                store?.let {
                    value(it.data)
                    changes.values() handledBy it.update
                }

                init?.invoke(this)
            }
            validationMsgs?.render { msg ->
                msg?.let {
                    div("absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none") {
                        icon("h-5 w-5 text-red-500", Solid.exclamation_circle)
                    }
                }
            }
            trailing?.let {
            }
        }

        validationMsgs?.render { msg ->
            if (msg != null) {
                p(
                    baseClass = "mt-2 text-sm text-red-600",
                    id = "$id-validation"
                ) { +msg.message }
            } else renderHelpText(helpText)
        } ?: renderHelpText(helpText)
    }
}
