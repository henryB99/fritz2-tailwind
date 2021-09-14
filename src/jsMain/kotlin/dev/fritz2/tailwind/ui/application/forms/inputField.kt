package dev.fritz2.tailwind.ui.application.forms

import dev.fritz2.binding.Store
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.*
import dev.fritz2.dom.values
import dev.fritz2.identification.uniqueId
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.hooks.*
import dev.fritz2.tailwind.hooks.hook
import dev.fritz2.tailwind.ui.*
import dev.fritz2.tailwind.ui.icons.Solid
import dev.fritz2.tailwind.validation.validationMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.Map

internal fun RenderContext.renderHelpText(helpText: String?): Unit {
    helpText?.let {
        p(baseClass = "mt-2 text-sm text-gray-500", id = "$id-description") {
            +it
        }
    }
}

/*
inputField("ssdkfn skdfjnsdk skdfjnskd ksdjfnskdjf ksdjfnsk sdkjfbsdk") {
    label("myLabel") { // Label.() ->
        className("sdkjfnskdj")
    }

    helpText("kajsdbkjsbfsjdh") {
    }

    value(myStore)

    element {
        keyUps handled
        disabled ...
    }

    items {
    }

}
 */

open class InputField(initializer: Initializer<InputField>) : Component<Div> {

    val value = InputDatabindingHook()
    // TODO: Fill with useful functionality!
    //  Or find something else to grab and work with the ``Flow<T?>?``
    //val validation = ValidationMessageHook(value)

    val disabled = BooleanAttributeHook(Input::disabled, Input::disabled)
    val type = AttributeHook(Input::type, Input::type)
    val placeholder = AttributeHook(Input::placeholder, Input::placeholder)
    val describedBy = RawAttributeHook<String, Input>("aria-describedby")

    var label: String? = null
    var helpText: String? = null
    var trailing: (Div.() -> Unit)? = null
    //var store: Store<String>? = null

    override fun RenderContext.render(classes: String?, id: String?): Div {
        // TODO: find abstraction for handling with messages (or null flow or no flow at all)
        val validationMsgs = value?.messages

        return div(classes) {
            label?.let {
                label("block text-sm font-medium text-gray-700") {
                    id?.let { `for`(id) }
                    +it
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
                    hooks(
                        type,
                        placeholder,
                        describedBy,
                        value,
                        disabled
                    )

                    //init?.invoke(this)
                }
                validationMsgs?.render { msg ->
                    msg?.let {
                        div("absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none") {
                            icon("h-5 w-5 text-red-500") { content(Solid.exclamation_circle) }
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

    init {
        initializer()
    }
}

fun RenderContext.inputField(
    classes: String? = null,
    id: String? = null,
    init: Initializer<InputField>
): Div = InputField(init).run { render(classes, id) }

/*
fun RenderContext.inputFieldOld(
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
                        icon("h-5 w-5 text-red-500") { content(Solid.exclamation_circle) }
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

*/