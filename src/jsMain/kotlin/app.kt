import dev.fritz2.binding.RootStore
import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.render
import dev.fritz2.tailwind.ui.checkboxGroup
import dev.fritz2.tailwind.ui.radioGroup
import dev.fritz2.tailwind.ui.selectBox
import dev.fritz2.tailwind.ui.toggle
import dev.fritz2.tailwind.validation.WithValidator
import kotlinx.coroutines.flow.map
import model.Framework
import model.FrameworkValidator
import model.L

external fun require(module: String): dynamic

fun main() {
    require("./styles.css")

    val frameworkStore = object : RootStore<Framework>(Framework("fritz2", 17, false)), WithValidator<Framework, Unit> {
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
        div("m-10") {
            frameworkStore.data.asText()
        }
//
//        clickButton("m-10") {
//            leftIcon(Solid.arrow_down)
//            label("herunterladen")
//        } handledBy frameworkStore.handle {
//            window.alert("Bin da")
//            it
//        }
//
        toggle {
            label("Hallo Welt")
            value(store = frameworkStore.sub(L.Framework.bool))
        }

        val selectionStore = storeOf("")
        val selectionStore2 = storeOf("zwei")
        val selectionStore3 = storeOf("zwei")
        val selectionStore4 = storeOf(listOf("drei"))

        div {
            p { selectionStore.data.map { "store1: $it" }.asText() }
            p { selectionStore2.data.map { "store2: $it" }.asText() }
            p { selectionStore3.data.map { "store3: $it" }.asText() }
            p { selectionStore4.data.map { "store4: $it" }.asText() }
        }

        val opts = listOf("eins", "zwei", "drei", "vier")

        selectBox<String>("m-10") {
            options(opts)
            value(selectionStore)
        }

        selectBox<String>("m-10") {
            options(opts) {
                +"Hier steht dann $it mit der Länge ${it.length}"
            }
            value(
                id = "hallo",
                data = selectionStore2.data,
                handler = {
                    it handledBy selectionStore2.update
                }
            )
        }

        radioGroup<String>("m-10") {
            options(opts)
            value(selectionStore3)
        }

        checkboxGroup<String>("m-10") {
            options(opts)
            value(
                id = "hugo",
                data = selectionStore4.data,
                handler = { v ->
                    v handledBy selectionStore4.handle { list, a ->
                        console.log("updating from $list -> $a")
                        a
                    }
                }
            )
        }

    }
}