package dev.fritz2.tailwind.hooks

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Svg
import dev.fritz2.tailwind.ui.IconDefinition
import export
import kotlinx.coroutines.flow.Flow

class IconHook : TagHook<RenderContext, Svg>() {
    private fun RenderContext.renderSvg(classes: String?, content: String, init: (Svg.() -> Unit)?): Svg =
        svg(classes) {
            xmlns("http://www.w3.org/2000/svg")
            viewBox("0 0 20 20")
            fill("currentColor")
            attr("aria-hidden", "true")
            content(content)
            if (init != null) init()
        }

    operator fun invoke(value: IconDefinition, init: (Svg.() -> Unit)? = null) = this.apply {
        apply = { classes ->
            renderSvg(classes, value, init)
        }
    }

    operator fun invoke(value: Flow<IconDefinition>, init: (Svg.() -> Unit)? = null) = this.apply {
        apply = { classes ->
            export {
                value.render {
                    export(renderSvg(classes, it, init))
                }
            }
        }
    }
}