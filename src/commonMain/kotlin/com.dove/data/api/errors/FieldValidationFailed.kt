package com.dove.data.api.errors

import kotlinx.serialization.SerialName
import kotlin.reflect.KProperty

@SerialName("FieldValidationFailed")
class FieldValidationFailed(fieldName: String, description: String) : ApiError {
    override val code: Int = 1
    override val message: String = "Field with name `$fieldName` is not applicable for request: $description"
}

fun FieldValidationFailed(property: KProperty<*>, description: String): FieldValidationFailed = FieldValidationFailed(
    property.name, description
)