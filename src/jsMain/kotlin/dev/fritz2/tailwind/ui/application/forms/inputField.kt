package dev.fritz2.tailwind.ui.application.forms

import dev.fritz2.binding.Store
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.*
import dev.fritz2.dom.values
import dev.fritz2.identification.uniqueId
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.hooks.Initializer
import dev.fritz2.tailwind.hooks.InputDatabindingHook
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

/*
abstract class ElementHook<T>(val render: RenderContext.(String?) -> Unit) {
    lateinit var apply: T.(String) -> Unit
}

class LabelHook : ElementHook<Label>({ classes ->
    label(classes) {
    }
}) {
    operator fun invoke(text: String) {
        render
    }
}

 */

abstract class TagFunctionHook<T, C> {
    var function: (C.() -> Unit)? = null

    fun hook(context: C) {
        function?.let { it(context) }
    }

    abstract operator fun invoke(value: T)
    abstract operator fun invoke(value: Flow<T>)
}


fun <T, C> C.hook(h: TagFunctionHook<T, C>) {
    h.hook(this)
}

class PlaceholderHook : TagFunctionHook<String, Input>() {

    override operator fun invoke(value: String) {
        function = { placeholder(value) }
    }

    override operator fun invoke(value: Flow<String>) {
        function = { placeholder(value) }
    }
}

class TypeHook : TagFunctionHook<String, Input>() {

    override operator fun invoke(value: String) {
        function = { type(value) }
    }

    override operator fun invoke(value: Flow<String>) {
        function = { type(value) }
    }
}

class AttributeHook<T>(private val name: String) : TagFunctionHook<T, Tag<*>>() {
    override fun invoke(value: T) {
        function = { attr(name, value) }
    }

    override fun invoke(value: Flow<T>) {
        function = { attr(name, value) }
    }

    fun invoke(value: Boolean, trueValue: String = "") {
        function = { attr(name, value, trueValue) }
    }

    fun invoke(value: Flow<Boolean>, trueValue: String = "") {
        function = { attr(name, value, trueValue) }
    }

    fun invoke(values: List<String>, separator: String = " ") {
        function = { attr(name, values, separator) }
    }

    fun invoke(values: Flow<List<String>>, separator: String = " ") {
        function = { attr(name, values, separator) }
    }

    fun invoke(values: Map<String, Boolean>, separator: String = " ") {
        function = { attr(name, values, separator) }
    }

    fun invoke(values: Flow<Map<String, Boolean>>, separator: String = " ") {
        function = { attr(name, values, separator) }
    }
}

object AttributeHookFactories {
    fun password() = AttributeHook<String>("password")
    fun type() = AttributeHook<String>("type")
}

object ButtonHooks {
    fun value() = AttributeHook<String>("value")
}

object InputHooks {
    fun type() = AttributeHook<String>("type")

    class ValueHook : TagFunctionHook<String, Input>() {

        override operator fun invoke(value: String) {
            function = { value(value) }
        }

        override operator fun invoke(value: Flow<String>) {
            function = { value(value) }
        }
    }

    fun value() = ValueHook()
}

open class InputField(initializer: Initializer<InputField>) : Component<Div> {

    val value = InputDatabindingHook()

    var label: String? = null
    //var type: String = "text"
    val type = InputHooks.type()

    //var placeholder: String? = null
    val placeholder = PlaceholderHook()
    var helpText: String? = null
    //var describedBy: String? = null
    val describedBy = AttributeHook<String>("aria-describedby")
    var trailing: (Div.() -> Unit)? = null
    var store: Store<String>? = null

    override fun RenderContext.render(classes: String?, id: String?): Div {
        val validationMsgs = store?.validationMessage()

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
                    //type(type)
                    hook(type)
                    //placeholder?.let { placeholder(it) }
                    hook(placeholder)
                    //describedBy?.let { attr("aria-describedby", it) }
                    hook(describedBy)

                    hook(value)

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
