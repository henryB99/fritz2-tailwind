package dev.fritz2.tailwind.ui.buttons

import dev.fritz2.dom.DomListener
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.tailwind.IconHook
import dev.fritz2.tailwind.Initializer
import dev.fritz2.tailwind.TextHook
import dev.fritz2.tailwind.hook
import exportEvent
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.events.MouseEvent


/*
 * TODO: add component interface
 */
class ClickButton(initializer: Initializer<ClickButton>) {
    val label = TextHook()
    val leftIcon = IconHook()
    val rightIcon = IconHook()

    /*
     * ToDo: render function to allow private attributes
     */

    init {
        initializer()
    }
}


fun RenderContext.clickButton(
    classes: String? = null,
    id: String? = null,
    init: Initializer<ClickButton>
): DomListener<MouseEvent, HTMLButtonElement> {
    val component = ClickButton(init)

    return exportEvent {
        button(
            "$classes inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-base font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500",
            id = id
        ) {
            type("button")

            hook(component.leftIcon, "-ml-1 mr-3 h-5 w-5")
            hook(component.label)
            hook(component.rightIcon, "ml-3 -mr-1 h-5 w-5")

            export(clicks)
        }
    }
}
