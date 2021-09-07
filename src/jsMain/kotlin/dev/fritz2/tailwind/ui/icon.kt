package dev.fritz2.tailwind.ui

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.tailwind.IconHook
import dev.fritz2.tailwind.Initializer
import dev.fritz2.tailwind.hook
import dev.fritz2.tailwind.ui.icons.Solid

typealias IconDefinition = String

/*
 * component
 *
 */
class Icon(initializer: Initializer<Icon>) {
    val content = IconHook()

    init {
        initializer()
    }
}

/*
 * factory
 */
fun RenderContext.icon(classes: String?, init: Initializer<Icon>): Unit {
    val component = Icon(init)

    hook(component.content, classes)
}

/*
 * shortcut methods for often used combinations as a one-liner
 */
fun RenderContext.solidIcon(classes: String? = null, init: Solid.() -> IconDefinition) =
    icon(classes) {
        content(Solid.init())
    }

