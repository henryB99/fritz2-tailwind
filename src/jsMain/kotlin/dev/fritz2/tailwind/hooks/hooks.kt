package dev.fritz2.tailwind.hooks

typealias Initializer<T> = T.() -> Unit

interface Hook<T> {
    val isSet: Boolean

    fun use(other: T)
}

abstract class SimpleHook<T> : Hook<SimpleHook<T>> {
    var apply: (T.() -> Unit)? = null

    override fun use(other: SimpleHook<T>) {
        apply = other.apply
    }

    override val isSet: Boolean
        get() = (apply != null)
}

fun <T> T.hook(h: SimpleHook<T>) = h.apply?.invoke(this)

fun <T> T.hooks(vararg h: SimpleHook<T>) = h.forEach { it.apply?.invoke(this) }


abstract class TagHook<T, E> : Hook<TagHook<T, E>> {
    var apply: (T.(String?) -> E)? = null

    override val isSet: Boolean
        get() = (apply != null)

    override fun use(other: TagHook<T, E>) {
        apply = other.apply
    }
}

fun <T, E> T.hook(h: TagHook<T, E>, classes: String? = null) = h.apply?.invoke(this, classes)




