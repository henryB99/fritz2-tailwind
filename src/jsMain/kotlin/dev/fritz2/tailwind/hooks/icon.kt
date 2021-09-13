package dev.fritz2.tailwind.hooks

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Svg
import dev.fritz2.tailwind.ui.IconDefinition
import export
import kotlinx.coroutines.flow.Flow

class IconHook : TagHook<RenderContext, Svg>() {
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
            export<Svg> {
                value.render {
                    export(renderSvg(classes, it))
                }
            }
        }
    }
}