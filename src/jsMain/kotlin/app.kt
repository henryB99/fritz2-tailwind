import dev.fritz2.binding.RootStore
import dev.fritz2.dom.html.render
import dev.fritz2.tailwind.ui.application.forms.inputField
import dev.fritz2.tailwind.ui.application.forms.toggleField
import dev.fritz2.tailwind.ui.buttons.clickButton
import dev.fritz2.tailwind.ui.icons.Solid
import dev.fritz2.tailwind.ui.toggle
import dev.fritz2.tailwind.validation.WithValidator
import kotlinx.browser.window
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

        inputField("m-10",
            label = "Testlabel",
            store = frameworkStore.sub(L.Framework.name),
            helpText = "some help may be good...",
            trailing = { span("text-gray-500 sm:text-sm") { +"EUR" } }
        )

        div("m-10") {
            frameworkStore.data.asText()
        }

        clickButton("m-10") {
            leftIcon(Solid.arrow_down)
            label("herunterladen")
        } handledBy frameworkStore.handle {
            window.alert("Bin da")
            it
        }

        toggle {
            label("Hallo Welt")
            value(store = frameworkStore.sub(L.Framework.bool))
        }

        toggleField(label = "Hugo", store = frameworkStore.sub(L.Framework.bool))

    }
}