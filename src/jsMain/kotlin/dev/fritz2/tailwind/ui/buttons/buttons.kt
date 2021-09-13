package dev.fritz2.tailwind.ui.buttons

import TextHook
import dev.fritz2.dom.DomListener
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.hooks.IconHook
import dev.fritz2.tailwind.hooks.Initializer
import dev.fritz2.tailwind.hooks.LoadingHook
import dev.fritz2.tailwind.hooks.hook
import export
import hook
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.events.MouseEvent


class ClickButton(initializer: Initializer<ClickButton>) : Component<DomListener<MouseEvent, HTMLButtonElement>> {
    val label = TextHook()
    val leftIcon = IconHook()
    val rightIcon = IconHook()
    val loading = LoadingHook()

    override fun RenderContext.render(classes: String?, id: String?): DomListener<MouseEvent, HTMLButtonElement> =
        export {
            button(
                "$classes inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-base font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500",
                id = id
            ) {
                type("button")

                val leftIconClasses = "-ml-1 mr-3 h-5 w-5"
                if (leftIcon.isSet) hook(loading, leftIconClasses) {
                    hook(leftIcon, leftIconClasses)
                }

                if (!(leftIcon.isSet || rightIcon.isSet)) {
                    hook(loading, "mx-3 h-5 -w-5") {
                        span { hook(label) }
                    }
                } else span { hook(label) }

                val rightIconClasses = "-mr-1 ml-3 h-5 w-5"
                if (rightIcon.isSet) hook(loading, rightIconClasses) {
                    hook(rightIcon, rightIconClasses)
                }

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

