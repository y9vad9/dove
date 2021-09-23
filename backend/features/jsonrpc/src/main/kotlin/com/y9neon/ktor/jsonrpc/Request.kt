package com.y9neon.ktor.jsonrpc

import kotlin.reflect.KClass

public inline fun <reified T : Any> Request(
    method: String,
    noinline body: suspend JsonRpcSession.(T) -> Unit
): Request<T> {
    return Request<T>(method, body, T::class)
}

public data class Request<out T : Any> @PublishedApi internal constructor(
    public val method: String,
    private val body: suspend JsonRpcSession.(@UnsafeVariance T) -> Unit,
    internal val resultClass: KClass<@UnsafeVariance T>
) {
    public suspend fun execute(session: JsonRpcSession, params: @UnsafeVariance T) {
        body(session, params)
    }
}