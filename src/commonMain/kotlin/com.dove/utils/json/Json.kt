package com.dove.utils.json

import kotlinx.serialization.json.Json

val json: Json = Json {
    encodeDefaults = true
}