package com.y9neon.ktor.jsonrpc

import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.serializer
import io.ktor.websocket.webSocket as webSocketDefault

@OptIn(InternalSerializationApi::class)
public fun Routing.webSocket(path: String, block: JsonRpcRequestsBuilder.() -> Unit) {
    val builder = JsonRpcRequestsBuilder()
    webSocketDefault(path) {
        val session = JsonRpcSession(this)
        for (frame in incoming) {
            val serialized = Json.decodeFromString<JsonObject>((frame as? Frame.Text ?: continue).readText())
            if (!serialized.containsKey("method"))
                continue
            val request = builder.requests.firstOrNull {
                it.method == serialized["method"]!!.toString()
            } ?: continue
            request.execute(
                session,
                Json.decodeFromJsonElement(request.resultClass.serializer(), serialized["params"]!!.jsonObject)
            )
        }
    }
}