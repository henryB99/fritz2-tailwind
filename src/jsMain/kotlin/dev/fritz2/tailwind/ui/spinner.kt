package dev.fritz2.tailwind.ui

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Svg


fun RenderContext.spinner(classes: String? = null, id: String? = null): Svg =
    svg("$classes animate-spin") {
        xmlns("http://www.w3.org/2000/svg")
        fill("none")
        viewBox("0 0 24 24")
        custom("circle", "opacity-25") {
            attr("cx", "12")
            attr("cy", "12")
            attr("r", "10")
            attr("stroke", "currentColor")
            attr("stroke-width", "4")
        }
        path("opacity-75") {
            attr("fill", "currentColor")
            d("M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z")
        }
    }

