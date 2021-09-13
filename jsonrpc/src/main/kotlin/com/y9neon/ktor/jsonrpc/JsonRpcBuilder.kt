package com.y9neon.ktor.jsonrpc

import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.cancel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.atomic.AtomicLong
import kotlin.reflect.KClass

public class JsonRpcRequestsBuilder internal constructor() {
    internal val requests: MutableList<Request<out Any>> = mutableListOf()

    /**
     * Registers new request.
     * @param method - method name.
     */
    public fun <T : Any> request(method: String, resultClass: KClass<T>, block: suspend JsonRpcSession.(T) -> Unit) {
        requests += Request(method, block, resultClass)
    }
}

public inline fun <reified T : Any> JsonRpcRequestsBuilder.request(
    method: String,
    noinline body: suspend JsonRpcSession.(T) -> Unit
) {
    request(method, T::class, body)
}

public class JsonRpcSession private constructor(
    @PublishedApi internal val session: DefaultWebSocketSession,
    public val id: Long
) {
    private companion object {
        var lastId: AtomicLong = AtomicLong(0)
    }

    public constructor(session: DefaultWebSocketSession) : this(session, lastId.incrementAndGet())

    public suspend inline fun <reified T : Any> send(result: T) {
        session.send(Frame.Text(Json.encodeToString(result)))
    }

    public fun cancel() {
        session.cancel()
    }
}