package dev.fritz2.tailwind.ui

import TextHook
import dev.fritz2.dom.html.P
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.hooks.Initializer
import dev.fritz2.identification.Id
import hook

class ValidationMessageLabel(initializer: Initializer<ValidationMessageLabel>) : Component<P> {

    val message = TextHook()

    override fun RenderContext.render(classes: String?, id: String?): P {
        val componentId = id ?: Id.next()
        return p(
            baseClass = "mt-2 text-sm text-red-600",
            id = "$componentId-validation"
        ) { hook(message) }
    }

    init {
        initializer()
    }
}

fun RenderContext.validationMessageLabel(
    classes: String? = null,
    id: String? = null,
    init: Initializer<ValidationMessageLabel>
): P = ValidationMessageLabel(init).run { render(classes, id) }
