package dev.fritz2.tailwind.ui.buttons

import dev.fritz2.dom.DomListener
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.ui.hooks.IconHook
import dev.fritz2.tailwind.ui.hooks.Initializer
import dev.fritz2.tailwind.ui.hooks.TextHook
import dev.fritz2.tailwind.ui.hooks.hook
import exportEvent
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.events.MouseEvent


class ClickButton(initializer: Initializer<ClickButton>) : Component<DomListener<MouseEvent, HTMLButtonElement>> {
    val label = TextHook()
    val leftIcon = IconHook()
    val rightIcon = IconHook()

    override fun RenderContext.render(classes: String?, id: String?): DomListener<MouseEvent, HTMLButtonElement> =
        exportEvent {
            button(
                "$classes inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-base font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500",
                id = id
            ) {
                type("button")

                hook(leftIcon, "-ml-1 mr-3 h-5 w-5")
                hook(label)
                hook(rightIcon, "ml-3 -mr-1 h-5 w-5")

                export(clicks)
            }
        }

    init {
        initializer()
    }
}


fun RenderContext.clickButton(
    classes: String? = null,
    id: String? = null,
    init: Initializer<ClickButton>
): DomListener<MouseEvent, HTMLButtonElement> = ClickButton(init).run { render(classes, id) }

