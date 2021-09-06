package com.dove.server.utils.openapi

import com.dove.data.api.errors.ApiError
import com.dove.data.monad.Either
import com.dove.data.monad.onError
import com.dove.data.monad.onSuccess
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.response.respond
import io.ktor.http.*
import com.papsign.ktor.openapigen.route.path.normal.delete as defaultDelete
import com.papsign.ktor.openapigen.route.path.normal.get as defaultGet
import com.papsign.ktor.openapigen.route.path.normal.post as defaultPost
import com.papsign.ktor.openapigen.route.path.normal.put as defaultPut

inline fun <reified TParam : Any, reified TResponse : Any> NormalOpenAPIRoute.getNullable(
    crossinline block: suspend TParam.() -> TResponse?
) = defaultGet<TParam, TResponse> { parameters ->
    respond(
        block(parameters) ?: return@defaultGet responder.respond(
            HttpStatusCode.BadRequest, "{}", pipeline
        )
    )
}

inline fun <reified TParam : Any, reified TResponse : Any> NormalOpenAPIRoute.get(
    crossinline block: suspend TParam.() -> Either<TResponse, ApiError>
) = defaultGet<TParam, TResponse> { parameters ->
    block(parameters).onSuccess {
        respond(it)
    }.onError {
        respondError(it)
    }
}

inline fun <reified TParams : Any, reified TResponse : Any, reified TRequest : Any> NormalOpenAPIRoute.put(
    crossinline block: suspend TParams.(TRequest) -> Either<TResponse, ApiError>
) = defaultPut<TParams, TResponse, TRequest> { parameters, request ->
    block(parameters, request).onSuccess { response ->
        respond(response)
    }.onError {
        respondError(it)
    }
}

inline fun <reified TParam : Any, reified TResponse : Any> NormalOpenAPIRoute.delete(
    crossinline block: suspend TParam.() -> Either<TResponse, ApiError>
) = defaultDelete<TParam, TResponse> { parameters ->
    block(parameters).onSuccess {
        respond(it)
    }.onError {
        respondError(it)
    }
}

inline fun <reified TParams : Any, reified TResponse : Any, reified TRequest : Any> NormalOpenAPIRoute.post(
    crossinline block: suspend TParams.(TRequest) -> Either<TResponse, ApiError>
) = defaultPost<TParams, TResponse, TRequest> { parameters, request ->
    block(parameters, request).onSuccess { response ->
        respond(response)
    }.onError {
        respondError(it)
    }
}