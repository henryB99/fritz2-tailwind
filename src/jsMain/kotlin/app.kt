import dev.fritz2.binding.RootStore
import dev.fritz2.binding.storeOf
import dev.fritz2.dom.html.render
import dev.fritz2.tailwind.ui.application.forms.inputField
import dev.fritz2.tailwind.ui.buttons.clickButton
import dev.fritz2.tailwind.ui.checkboxGroup
import dev.fritz2.tailwind.ui.icons.Solid
import dev.fritz2.tailwind.ui.radioGroup
import dev.fritz2.tailwind.ui.selectBox
import dev.fritz2.tailwind.ui.toggle
import dev.fritz2.tailwind.validation.WithValidator
import dev.fritz2.tailwind.validation.validationMessage
import dev.fritz2.tracking.tracker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import model.Framework
import model.FrameworkValidator
import model.L

external fun require(module: String): dynamic

fun main() {
    require("./styles.css")

    val frameworkStore = object : RootStore<Framework>(Framework("fritz2", 17, false)), WithValidator<Framework, Unit> {
        override val validator = FrameworkValidator
        val loading = tracker()

        val longRunning = handle {
            loading.track("running...") {
                delay(3000)
                it
            }
        }

        init {
            validate(Unit)
        }
    }

    render {

        div("m-10") {
            frameworkStore.data.asText()
        }

        inputField("m-10", "myInput") {
            label("Testlabel")
            type("password")
            disabled(frameworkStore.sub(L.Framework.bool).data)
            with(frameworkStore.sub(L.Framework.name)) {
                value("input", this.data, this.validationMessage()) { it handledBy this@with.update }
            }
            //value(frameworkStore.sub(L.Framework.name))

            helpText("some help may be good...")
            //store = frameworkStore.sub(L.Framework.name)
            //trailing({ span("text-gray-500 sm:text-sm") { +"EUR" } })
        }

        div("flex w-full") {
            clickButton("m-10") {
                icon(Solid.arrow_left)
                label("herunterladen")
            } handledBy frameworkStore.longRunning

            clickButton("m-10") {
                label("herunterladen")
            } handledBy frameworkStore.longRunning

            clickButton("m-10") {
                icon(Solid.arrow_down)
            } handledBy frameworkStore.longRunning

            clickButton("m-10") {
                icon(Solid.arrow_right).right()
                label("herunterladen")
            } handledBy frameworkStore.longRunning
        }


        div("flex w-full") {
            clickButton("m-10") {
                icon(Solid.arrow_left)
                label("herunterladen")
                loading(frameworkStore.loading.data)
            } handledBy frameworkStore.longRunning

            clickButton("m-10") {
                label("herunterladen")
                loading(frameworkStore.loading.data)
            } handledBy frameworkStore.longRunning

            clickButton("m-10") {
                icon(Solid.arrow_down) {
                    className("text-yellow-400")
                }
                loading(frameworkStore.loading.data)
            } handledBy frameworkStore.longRunning

            clickButton("m-10") {
                icon(Solid.arrow_right).right()
                label("herunterladen")
                loading(frameworkStore.loading.data)
            } handledBy frameworkStore.longRunning
        }
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
            label("Bitte wählen")
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
            label("Bitte wählen...")
            options(opts)
            value(selectionStore3)
        }

        checkboxGroup<String>("m-10") {
            label("Bitte wählen...")
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