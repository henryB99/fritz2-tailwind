package dev.fritz2.tailwind.ui

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.IconHook
import dev.fritz2.tailwind.Initializer
import dev.fritz2.tailwind.hook
import dev.fritz2.tailwind.ui.icons.Solid

typealias IconDefinition = String

/*
 * component
 */
class Icon(initializer: Initializer<Icon>) : Component<Unit> {
    val content = IconHook()

    override fun RenderContext.render(classes: String?, id: String?): Unit {
        hook(content, classes)
    }

    init {
        initializer()
    }
}

/*
 * factory
 */
fun RenderContext.icon(classes: String? = null, id: String? = null, init: Initializer<Icon>): Unit =
    Icon(init).run { render(classes, id) }

/*
 * shortcut methods for often used combinations as a one-liner
 */
fun RenderContext.solidIcon(classes: String? = null, id: String? = null, init: Solid.() -> IconDefinition) =
    icon(classes, id) {
        content(Solid.init())
    }

