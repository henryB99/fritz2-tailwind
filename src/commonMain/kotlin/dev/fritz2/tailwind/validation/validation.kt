package dev.fritz2.tailwind.validation

import dev.fritz2.identification.Inspector
import dev.fritz2.identification.inspect
import dev.fritz2.validation.ValidationMessage
import dev.fritz2.validation.Validator

/**
 * Special [ValidationMessage] for fritz2 components.
 *
 * **Important**: [id] should be generated by using the [inspect] function
 * in your [Validator].
 * By default the validation fails if one or more [ComponentValidationMessage]s have
 * a [severity] of [Severity.Error]. You can override the [isError] method to change this
 * behavior.
 *
 * @param id generated by [Inspector]
 * @param severity used for rendering the [ValidationMessage]
 * @param message contains the message
 * @param details optional details for extending the message
 */
data class ComponentValidationMessage(
    val id: String,
    val severity: Severity,
    val message: String,
    val details: String? = null,
) : ValidationMessage {
    override fun isError(): Boolean = severity > Severity.Warning
}

/**
 * Enum which specify the [Severity] of [ComponentValidationMessage].
 */
enum class Severity {
    Info, Success, Warning, Error
}

/**
 * Creates [ComponentValidationMessage] with [Severity.Info].
 *
 * @param id generated by [Inspector]
 * @param @param message contains the message
 * @param details optional details for extending the message
 */
fun infoMessage(id: String, message: String, details: String? = null) =
    ComponentValidationMessage(id, Severity.Info, message, details)

/**
 * Creates [ComponentValidationMessage] with [Severity.Info].
 *
 * @param id generated by [Inspector]
 * @param @param message contains the message
 * @param details optional details for extending the message
 */
fun successMessage(id: String, message: String, details: String? = null) =
    ComponentValidationMessage(id, Severity.Success, message, details)

/**
 * Creates [ComponentValidationMessage] with [Severity.Warning].
 *
 * @param id generated by [Inspector]
 * @param @param message contains the message
 * @param details optional details for extending the message
 */
fun warningMessage(id: String, message: String, details: String? = null) =
    ComponentValidationMessage(id, Severity.Warning, message, details)

/**
 * Creates [ComponentValidationMessage] with [Severity.Error].
 *
 * @param id generated by [Inspector]
 * @param @param message contains the message
 * @param details optional details for extending the message
 */
fun errorMessage(id: String, message: String, details: String? = null) =
    ComponentValidationMessage(id, Severity.Error, message, details)

/**
 * Special [Validator] for fritz2 components which uses the [ComponentValidationMessage]
 * internally.
 */
abstract class ComponentValidator<D, T> : Validator<D, ComponentValidationMessage, T>()
