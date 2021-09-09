package dev.fritz2.tailwind.ui

import dev.fritz2.binding.Handler
import dev.fritz2.binding.Store
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.Button
import dev.fritz2.dom.html.Input
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.values
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.Initializer
import dev.fritz2.tailwind.TextHook
import dev.fritz2.tailwind.hook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.w3c.dom.Element


open class DatabindingHook<T, X, Y : Tag<*>>(
    inline val action: Y.() -> Flow<X>,
    inline val handler: Store<T>.() -> Handler<X>,
    inline val applyValues: Y.(Flow<T>) -> Unit
) {
    lateinit var values: Flow<T>
    lateinit var apply: Y.() -> Unit
    var id: String? = null

    open operator fun invoke(id: String? = null, values: Flow<T>, handler: Y.(Flow<X>) -> Unit) {
        this.id = id
        this.values = values
        apply = {
            applyValues(values)
            handler(action())
        }
    }

    open operator fun invoke(store: Store<T>) {
        this.invoke(store.id, store.data) { it handledBy store.handler() }
    }
}

fun <T, E : Element, X, Y : Tag<E>> Y.hook(h: DatabindingHook<T, X, Y>) = h.apply.invoke(this)

class InputDatabindingHook : DatabindingHook<String, String, Input>(
    action = { changes.values() },
    handler = { update },
    applyValues = { value(it) }
)

class SwitchDatabindingHook : DatabindingHook<Boolean, Unit, Button>(
    action = { clicks.events.map {} },
    handler = { handle { !it } },
    applyValues = { attr("aria-checked", it, trueValue = "true") }
)


/*
toggle {
    value(myStore)

    value(id = ..., flow = ...) {
        selected handledBy myHandler
    }

    element {
        disabled()
        keyUps handledBy
    }

    events {

    }
}



inputField("ssdkfn skdfjnsdk skdfjnskd ksdjfnskdjf ksdjfnsk sdkjfbsdk") {
    label("myLabel") {
        className("sdkjfnskdj")
    }

    helpText("kajsdbkjsbfsjdh") {
    }

    value(myStore)

    element {
        keyUps handled
        disabled ...
    }

    items {
    }

}.domNode().also {
}

*/

class Toggle(initializer: Initializer<Toggle>, context: RenderContext) : Component<Button> {

    val label = TextHook()
    val value = SwitchDatabindingHook()

    override fun RenderContext.render(classes: String?, id: String?) =
        button(
            "bg-gray-200 relative inline-flex flex-shrink-0 h-6 w-11 border-2 border-transparent rounded-full cursor-pointer transition-colors ease-in-out duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 $classes",
            id = id ?: value.id // ?: generateId if necessary
        ) {
            type("button")
            attr("role", "switch")
            span("sr-only") { hook(label) }
            span(" pointer-events-none inline-block h-5 w-5 rounded-full bg-white shadow transform ring-0 transition ease-in-out duration-200") {
                /* <!-- Enabled: "translate-x-5", Not Enabled: "translate-x-0" --> */
                className(value.values.map { if (it) "translate-x-5" else "translate-x-0" })
                attr("aria-hidden", "true")
            }
            hook(value)
        }

    init {
        initializer()
    }
}

fun RenderContext.toggle(
    classes: String? = null,
    id: String? = null,
    init: Initializer<Toggle>
): Button = Toggle(init, this).run { render(classes, id) }
