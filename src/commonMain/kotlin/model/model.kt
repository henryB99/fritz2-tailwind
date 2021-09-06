package model

import dev.fritz2.identification.inspect
import dev.fritz2.lenses.Lenses
import dev.fritz2.tailwind.validation.ComponentValidationMessage
import dev.fritz2.tailwind.validation.ComponentValidator
import dev.fritz2.tailwind.validation.Severity

// Put your model data classes in here to use it on js and jvm side

@Lenses
data class Framework(val name: String, val num: Int, val bool: Boolean)

object FrameworkValidator : ComponentValidator<Framework, Unit>() {
    @OptIn(ExperimentalStdlibApi::class)
    override fun validate(data: Framework, metadata: Unit): List<ComponentValidationMessage> = buildList {
        val framework = inspect(data)

        framework.sub(L.Framework.name).apply {
            if (this.data.length < 3) add(ComponentValidationMessage(this.id, Severity.Error, "name is too short"))
        }

    }
}