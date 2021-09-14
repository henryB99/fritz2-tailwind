package dev.fritz2.tailwind.ui

import TextHook
import dev.fritz2.dom.html.Label
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.hooks.Initializer
import dev.fritz2.tailwind.hooks.hook


class TextLabel(initializer: Initializer<TextLabel>) : Component<Label> {

    val text = TextHook()

    override fun RenderContext.render(classes: String?, id: String?) =
        label("${classes.orEmpty()} block text-sm font-medium text-gray-700") {
            id?.let { `for`(it) }
            hook(text)
        }

    init {
        initializer()
    }
}

fun RenderContext.textLabel(
    classes: String? = null,
    id: String? = null,
    init: Initializer<TextLabel>
): Label = TextLabel(init).run { render(classes, id) }
