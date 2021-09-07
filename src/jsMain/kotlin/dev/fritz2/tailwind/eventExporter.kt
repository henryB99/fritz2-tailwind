import dev.fritz2.dom.DomListener
import dev.fritz2.tailwind.Initializer
import org.w3c.dom.Element
import org.w3c.dom.events.Event

/*
 * TODO: offer ElementExtractor and rename to EventExtractor
 */
class EventExporter<T : Event, E : Element>(initializer: Initializer<EventExporter<T, E>>) {
    lateinit var listener: DomListener<T, E>

    fun export(l: DomListener<T, E>) {
        listener = l
    }

    init {
        initializer()
    }
}

fun <T : Event, E : Element> exportEvent(scope: EventExporter<T, E>.() -> Unit): DomListener<T, E> =
    EventExporter(scope).listener
