package dev.fritz2.tailwind.hooks

typealias Initializer<T> = T.() -> Unit

interface Hook {
    val isSet: Boolean
}

abstract class SimpleHook<T> : Hook {
    var apply: (T.() -> Unit)? = null

    override val isSet: Boolean
        get() = (apply != null)
}

fun <T> T.hook(h: SimpleHook<T>) = h.apply?.invoke(this)


abstract class TagHook<T, E> : Hook {
    var apply: (T.(String?) -> E)? = null

    override val isSet: Boolean
        get() = (apply != null)
}

fun <T, E> T.hook(h: TagHook<T, E>, classes: String? = null) = h.apply?.invoke(this, classes)




