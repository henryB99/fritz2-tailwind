package dev.fritz2.tailwind.hooks

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.tailwind.ui.spinner
import kotlinx.coroutines.flow.Flow

class LoadingHook {

    var apply: (RenderContext.(String?, RenderContext.() -> Unit) -> Unit)? = null

    operator fun invoke(loading: Flow<Boolean>) {
        apply = { classes, renderElse ->
            loading.render { isLoading ->
                if (isLoading) spinner(classes) else renderElse()
            }
        }
    }

    val isSet: Boolean
        get() = apply != null
}

fun RenderContext.hook(hook: LoadingHook, classes: String? = null, renderElse: RenderContext.() -> Unit = {}) {
    if (hook.isSet) {
        hook.apply?.invoke(this, classes, renderElse)
    } else {
        renderElse()
    }
}
