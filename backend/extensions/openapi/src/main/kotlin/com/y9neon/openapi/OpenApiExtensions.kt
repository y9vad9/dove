package com.y9neon.openapi

import com.dove.data.api.ApiError
import com.dove.data.monad.Either
import com.dove.data.monad.onError
import com.dove.data.monad.onSuccess
import com.papsign.ktor.openapigen.route.OpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.y9neon.middleware.authorization.Authorization
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import com.papsign.ktor.openapigen.route.path.normal.delete as defaultDelete
import com.papsign.ktor.openapigen.route.path.normal.get as defaultGet
import com.papsign.ktor.openapigen.route.path.normal.patch as defaultPatch
import com.papsign.ktor.openapigen.route.path.normal.post as defaultPost
import com.papsign.ktor.openapigen.route.path.normal.put as defaultPut

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


inline fun <reified TParams : Any, reified TResponse : Any, reified TRequest : Any> NormalOpenAPIRoute.patch(
    crossinline block: suspend TParams.(TRequest) -> Either<TResponse, ApiError>
) = defaultPatch<TParams, TResponse, TRequest> { parameters, request ->
    block(parameters, request).onSuccess { response ->
        respond(response)
    }.onError {
        respondError(it)
    }
}

@ContextDsl
inline fun <TRoute : OpenAPIRoute<TRoute>> TRoute.authorized(
    feature: Authorization.Feature<*>,
    crossinline block: TRoute.() -> Unit
): TRoute = route("")
    .apply {
        block()

        val featureInstance = ktorRoute.application.featureOrNull(feature)
            ?: error("Authorization feature is not installed")

        ktorRoute.interceptOnRoute(ApplicationCallPipeline.Features) {
            featureInstance.authorizePipeline(context = this)
        }
    }

fun Route.interceptOnRoute(
    phase: PipelinePhase,
    interceptor: PipelineInterceptor<Unit, ApplicationCall>
) {
    fun installInterceptors(routes: List<Route>) {
        if (routes.isEmpty())
            return

        routes.forEach { route ->
            route.intercept(phase, interceptor)
        }

        installInterceptors(routes.flatMap(Route::children))
    }
    installInterceptors(listOf(this))
}

fun <T : Any, TRoute : OpenAPIRoute<TRoute>> TRoute.authorization(feature: Authorization.Feature<T>) =
    ktorRoute.attributes.getOrNull(feature.principalKey)
        ?: error("`authorized` is not set for current route")