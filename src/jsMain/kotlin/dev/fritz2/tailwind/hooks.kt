package dev.fritz2.tailwind

import dev.fritz2.dom.WithText
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Svg
import dev.fritz2.tailwind.ui.IconDefinition
import kotlinx.coroutines.flow.Flow

typealias Initializer<T> = T.() -> Unit

/*
 * TODO: second type parameter needed for return value?
 */
abstract class Hook<R> {
    var apply: (R.(String?) -> Unit)? = null

    val hook: R.(classes: String?) -> Unit
        get() = { classes ->
            apply?.let { it(classes) }
        }
}

/*
 * TODO: Add ElementHook returning subtype of element
 */

fun <T> T.hook(h: Hook<T>, classes: String? = null) {
    h.hook.invoke(this, classes)
}


class TextHook : Hook<WithText<*>>() {
    operator fun invoke(value: String) {
        apply = { +value }
    }

    operator fun invoke(value: Flow<String>) {
        apply = { value.asText() }
    }
}



class IconHook : Hook<RenderContext>() {
    private inline fun RenderContext.renderSvg(classes: String?, content: String): Svg =
        svg(classes) {
            xmlns("http://www.w3.org/2000/svg")
            viewBox("0 0 20 20")
            fill("currentColor")
            attr("aria-hidden", "true")
            content(content)
        }

    operator fun invoke(value: IconDefinition) {
        apply = { classes ->
            renderSvg(classes, value)
        }
    }

    operator fun invoke(value: Flow<IconDefinition>) {
        apply = { classes ->
//            lateinit var result: Svg
            value.render {
                renderSvg(classes, it)
            }
            //          return result
        }
    }
}