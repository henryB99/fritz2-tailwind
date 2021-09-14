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
    val icon = IconHook()
    val loading = LoadingHook()

    private var iconRight: Boolean = false
    fun IconHook.right(): IconHook = this.apply { iconRight = true }

    override fun RenderContext.render(classes: String?, id: String?): DomListener<MouseEvent, HTMLButtonElement> =
        export {
            button(
                "inline-flex justify-center items-center px-4 py-2 border border-transparent shadow-sm text-base font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 ${classes.orEmpty()}",
                id = id
            ) {
                type("button")

                val iconClasses =
                    "h-5 w-5 " + if (iconRight) "-mr-1 ml-3 order-last" else if (!label.isSet) "-mx-1" else "-ml-1 mr-3"

                if (icon.isSet) hook(loading, iconClasses) {
                    hook(icon, iconClasses)
                }

                if (!icon.isSet) {
                    hook(loading, "h-5 w-5 mx-3") {
                        span { hook(label) }
                    }
                } else span { hook(label) }

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

