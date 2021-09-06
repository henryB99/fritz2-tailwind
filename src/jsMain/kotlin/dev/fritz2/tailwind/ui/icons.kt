package dev.fritz2.tailwind.ui

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Svg

typealias IconDefinition = Svg.() -> Unit

fun RenderContext.icon(classes: String?, definition: IconDefinition): Svg =
    svg(classes) {
        xmlns("http://www.w3.org/2000/svg")
        viewBox("0 0 20 20")
        fill("currentColor")
        attr("aria-hidden", "true")
        definition()
    }

fun RenderContext.icon(definition: IconDefinition): Stylable<Svg> = { classes ->
    icon(classes, definition)
}


