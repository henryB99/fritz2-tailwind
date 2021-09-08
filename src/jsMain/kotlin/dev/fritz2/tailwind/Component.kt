package dev.fritz2.tailwind

import dev.fritz2.dom.html.RenderContext

interface Component<T> {
    fun RenderContext.render(classes: String?, id: String?): T
}
