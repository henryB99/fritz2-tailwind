package dev.fritz2.tailwind.ui.buttons

import dev.fritz2.dom.Listener
import dev.fritz2.dom.html.Button
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.tailwind.ui.IconDefinition
import dev.fritz2.tailwind.ui.icon
import org.w3c.dom.events.MouseEvent


fun RenderContext.clickButton(
    classes: String? = null,
    id: String? = null,
    init: Button.() -> Unit
): Listener<MouseEvent> {
    lateinit var clickHandler: Listener<MouseEvent>
    button(
        "inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-base font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 $classes",
        id = id
    ) {
        type("button")
        clickHandler = clicks
        init()
    }
    return clickHandler
}

fun RenderContext.clickButton(
    classes: String? = null,
    leftIcon: IconDefinition? = null,
    label: String? = null,
    rightIcon: IconDefinition? = null,
    id: String? = null
): Listener<MouseEvent> =
    clickButton(classes, id = id) {
        leftIcon?.let {
            icon("-ml-1 mr-3 h-5 w-5", leftIcon)
        }

        label?.let {
            +label
        }

        rightIcon?.let {
            icon("ml-3 -mr-1 h-5 w-5", rightIcon)
        }
    }

