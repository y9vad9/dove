package com.dove.server.utils.openapi

import com.papsign.ktor.openapigen.model.DataModel
import kotlinx.serialization.json.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

fun DataModel.kserialize(): JsonElement {
    fun Any?.toJsonElement(): JsonElement {
        return when (this) {
            is Number -> JsonPrimitive(this)
            is String -> JsonPrimitive(this)
            is Boolean -> JsonPrimitive(this)
            is Enum<*> -> JsonPrimitive(this.name)
            is JsonElement -> this
            else -> {
                if (this != null) System.err.println("The type $this is unknown")
                JsonNull
            }
        }
    }

    fun Map<String, *>.clean(): JsonObject {
        val map = filterValues {
            when (it) {
                is Map<*, *> -> it.isNotEmpty()
                is Collection<*> -> it.isNotEmpty()
                else -> it != null
            }
        }
        return JsonObject(map.mapValues { entry -> entry.value.toJsonElement() }.filterNot { it.value == JsonNull })
    }

    fun cvt(value: Any?): JsonElement {
        return when (value) {
            is DataModel -> value.kserialize()
            is Map<*, *> -> value.entries.associate { (key, value) -> Pair(key.toString(), cvt(value)) }.clean()
            is Iterable<*> -> JsonArray(value.mapNotNull { cvt(it) })
            else -> value.toJsonElement()
        }
    }
    return this::class.memberProperties.associateBy { it.name }.mapValues { (_, prop) ->
        @Suppress("UNCHECKED_CAST")
        cvt((prop as KProperty1<DataModel, *>).get(this))
    }.clean()
}