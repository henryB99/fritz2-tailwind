import dev.fritz2.dom.WithText
import dev.fritz2.tailwind.hooks.SimpleHook
import kotlinx.coroutines.flow.Flow

class TextHook : SimpleHook<WithText<*>>() {
    operator fun invoke(value: String) {
        apply = { +value }
    }

    operator fun invoke(value: Flow<String>) {
        apply = { value.asText() }
    }

}

fun WithText<*>.hook(h: TextHook) = h.apply?.invoke(this)
