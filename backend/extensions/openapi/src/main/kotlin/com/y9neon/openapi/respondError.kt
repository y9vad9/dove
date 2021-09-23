package com.y9neon.openapi

import com.papsign.ktor.openapigen.route.response.OpenAPIPipelineResponseContext
import io.ktor.http.*

suspend fun <T, E : Any> OpenAPIPipelineResponseContext<T>.respondError(error: E) {
    responder.respond(HttpStatusCode.BadRequest, error, pipeline)
}