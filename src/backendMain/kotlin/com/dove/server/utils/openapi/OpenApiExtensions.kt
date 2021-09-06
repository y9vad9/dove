package com.dove.server.utils.openapi

import com.dove.data.api.errors.ApiError
import com.dove.data.monad.Either
import com.dove.data.monad.onError
import com.dove.data.monad.onSuccess
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.path.normal.get as defaultGet

inline fun <reified TParam : Any, reified TResponse : Any> NormalOpenAPIRoute.get(
    crossinline block: suspend TParam.() -> Either<TResponse, ApiError>
) = defaultGet<TParam, TResponse> { parameters ->
    block(parameters).onSuccess {
        respond(it)
    }.onError {
        respondError(it)
    }
}