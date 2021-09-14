package dev.fritz2.tailwind.ui.application.forms

import TextHook
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.hooks.*
import dev.fritz2.tailwind.ui.icon
import dev.fritz2.tailwind.ui.icons.Solid
import dev.fritz2.tailwind.ui.textLabel
import dev.fritz2.tailwind.ui.validationMessageLabel
import kotlinx.coroutines.flow.map

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

class HelperTextHook : TagHook<RenderContext, Unit>() {
    //TODO: Flow and init... ?
    operator fun invoke(text: String) {
        apply = {
            p(baseClass = "mt-2 text-sm text-gray-500", id = "$id-description") {
                +text
            }
        }
    }
}

open class InputField(initializer: Initializer<InputField>) : Component<Div> {

    val value = InputDatabindingHook()
    val label = TextHook()
    val placeholder = AttributeHook(Input::placeholder, Input::placeholder)
    val helpText = HelperTextHook()
    val type = AttributeHook(Input::type, Input::type)
    val disabled = BooleanAttributeHook(Input::disabled, Input::disabled)
    val describedBy = RawAttributeHook<String, Input>("aria-describedby")

    // TODO: What's this good for?
    var trailing: (Div.() -> Unit)? = null

    override fun RenderContext.render(classes: String?, id: String?): Div {
        val componentId = id ?: value.id

        // TODO: find abstraction for handling with messages (or null flow or no flow at all)
        val validationMessages = value.messages

        return div(classes) {
            if (label.isSet) {
                textLabel(id = componentId) {
                    text.use(label)
                }
            }

            //      val error = "pr-10 border-red-300 text-red-900 placeholder-red-300 focus:outline-none focus:ring-red-500 focus:border-red-500 sm:text-sm rounded-md"
            //    val good  = "focus:ring-indigo-500 focus:border-indigo-500 block w-full pr-10 sm:text-sm border-gray-300 rounded-md"

            div("mt-1 relative rounded-md shadow-sm") {
                input(
                    "block w-full pr-10 sm:text-sm rounded-md",
                    id = componentId
                ) {
                    validationMessages?.let {
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
                validationMessages?.render { msg ->
                    msg?.let {
                        div("absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none") {
                            icon("h-5 w-5 text-red-500") { content(Solid.exclamation_circle) }
                        }
                    }
                }
                trailing?.let {
                }
            }

            /*
            Idee:
            validationMessages.renderNotNull { messages ->
                validationMessageLabel(id = componentId) {
                    message(messages.message)
                }
            }.orElse {
                hook(helpText)
            }
            // -> geht nicht, weil flow selber null sein kann!

            // Oder:
            renderUncertain {
                default { flow ->
                    // flow is present and not null!
                }
                onNull {
                    // flow is null or null on flow
                    // -> beats other two!
                }
                noValue {
                    // value on flow is null
                }
                noFlow {
                    // flow itself is null
                }
            }

            // Beispiel:
            renderUncertain {
                default { messages ->
                    validationMessageLabel(id = componentId) {
                        message(messages.message)
                    }
                }
                onNull {
                    hook(helpText)
                }
            }

             */

            validationMessages?.render { msg ->
                if (msg != null) {
                    validationMessageLabel(id = componentId) {
                        message(msg.message)
                    }
                } else hook(helpText)
            } ?: hook(helpText)
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
