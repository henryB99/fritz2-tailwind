package dev.fritz2.tailwind.ui

import dev.fritz2.binding.Handler
import dev.fritz2.binding.RootStore
import dev.fritz2.binding.Store
import dev.fritz2.dom.Tag
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.Option
import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.Select
import dev.fritz2.dom.selectedIndex
import dev.fritz2.tailwind.Component
import dev.fritz2.tailwind.Initializer
import dev.fritz2.tailwind.TextHook
import dev.fritz2.tailwind.hook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


open class SingleSelectionStore : RootStore<Int?>(null) {
    val toggle = handleAndEmit<Int, Int> { _, new ->
        emit(new)
        new
    }
}

abstract class OptionsHook<T, C : Tag<*>, O : Tag<*>> {
    var options: List<T>? = null
    abstract var renderOptionLabel: O.(T) -> Unit

    // oder inline val
    abstract val apply: C.(Flow<T>) -> Unit

    operator fun invoke(options: List<T>, renderOptionLabel: O.(T) -> Unit = this.renderOptionLabel) {
        this.options = options
        this.renderOptionLabel = renderOptionLabel
    }

    operator fun invoke(vararg options: T, renderOptionLabel: O.(T) -> Unit = this.renderOptionLabel) {
        this.options = options.toList()
        this.renderOptionLabel = renderOptionLabel
    }
}

class SelectOptionsHook<T> : OptionsHook<T, Select, Option>() {

    override var renderOptionLabel: Option.(T) -> Unit = { opt ->
        +opt.toString()
    }

    override val apply: Select.(Flow<T>) -> Unit = { data ->
        options?.forEach { opt ->
            option {
                renderOptionLabel(opt)
                selected(data.map { it == opt })
            }
        }
    }

}


class SingleSelectionDatabindingHook<T, X, C : Tag<*>, O : Tag<*>>(
    private val optionsHook: OptionsHook<T, C, O>,

    inline val action: C.() -> Flow<X>,
    inline val handler: Store<T>.() -> Handler<X>,
//    inline val applyData: Y.(Flow<T>) -> Unit
) {
    lateinit var data: Flow<T>
    lateinit var apply: C.() -> Unit
    var id: String? = null

    operator fun invoke(id: String? = null, data: Flow<T>, handler: C.(Flow<X>) -> Unit) {
        this.id = id
        this.data = data
        apply = {
            optionsHook.apply(this, data)

            handler(action())
        }
    }

    operator fun invoke(store: Store<T>) {
        this.invoke(store.id, store.data) { it handledBy store.handler() }
    }
}


class SelectBox<T>(initializer: Initializer<SelectBox<T>>) : Component<Div> {

    val label = TextHook()
    val options = SelectOptionsHook<T>()
    val value = SingleSelectionDatabindingHook<T, Int, Select, Option>(
        options,
        action = { changes.selectedIndex() },
        handler = { handle<Int> { old, index -> options.options?.get(index) ?: old } }
    )

    override fun RenderContext.render(classes: String?, id: String?) = div {
        if (label.isSet) {
            label("block text-sm font-medium text-gray-700") {
                `for`("location")
                hook(label)
            }
        }
        select(
            baseClass = "mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md",
            id = id ?: value.id,
        ) {
            value.apply.invoke(this)
        }
    }

    init {
        initializer()
    }
}

fun <T> RenderContext.selectBox(
    classes: String? = null,
    id: String? = null,
    init: Initializer<SelectBox<T>>
): Div = SelectBox(init).run { render(classes, id) }
