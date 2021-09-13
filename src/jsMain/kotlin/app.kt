import dev.fritz2.binding.RootStore
import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.render
import dev.fritz2.tailwind.ui.application.forms.inputField
import dev.fritz2.tailwind.ui.selectBox
import dev.fritz2.tailwind.validation.WithValidator
import kotlinx.coroutines.flow.map
import model.Framework
import model.FrameworkValidator
import model.L

external fun require(module: String): dynamic

fun main() {
    require("./styles.css")

    val frameworkStore = object : RootStore<Framework>(Framework("", 17, false)), WithValidator<Framework, Unit> {
        override val validator = FrameworkValidator

        init {
            validate(Unit)
        }
    }

    render {

//        inputField("m-10",
//            label = "Testlabel",
//            store = frameworkStore.sub(L.Framework.name),
//            helpText = "some help may be good...",
//            trailing = { span("text-gray-500 sm:text-sm") { +"EUR" } }
//        )
//
//        div("m-10") {
//            frameworkStore.data.asText()
//        }
//
//        clickButton("m-10") {
//            leftIcon(Solid.arrow_down)
//            label("herunterladen")
//        } handledBy frameworkStore.handle {
//            window.alert("Bin da")
//            it
//        }
//
//        toggle {
//            label("Hallo Welt")
//            value(store = frameworkStore.sub(L.Framework.bool))
//        }
//

        inputField("m-10", "myNewInputfield") {
            store = frameworkStore.sub(L.Framework.name)
            value(frameworkStore.sub(L.Framework.name))
            label = "Testlabel"
            helpText = "some help may be good..."
            placeholder("Hallo Welt!")
            describedBy("foo")
            type("password")
            trailing = { span("text-gray-500 sm:text-sm") { +"EUR" } }
        }

        val selectionStore = storeOf("")
        val selectionStore2 = storeOf("zwei")

        div {
            p { selectionStore.data.map { "store1: $it" }.asText() }
            p { selectionStore2.data.map { "store2: $it" }.asText() }
        }

        val opts = listOf("eins", "zwei", "drei", "vier")

        selectBox<String> {
            options(opts)
            value(selectionStore)
        }

        selectBox<String> {
            options(opts) {
                +"Hier steht dann $it mit der Länge ${it.length}"
            }
            value(
                id = "hallo",
                data = selectionStore2.data,
                handler = {
                    it handledBy selectionStore2.handle { _, index: Int ->
                        console.log("index: $index")
                        opts[index].also { console.log("returning $it") }
                    }
                }
            )
        }

    }
}