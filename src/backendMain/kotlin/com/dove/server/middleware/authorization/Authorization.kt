package com.dove.server.middleware.authorization

import com.dove.data.api.ApiResult
import com.dove.data.api.errors.ApiError
import com.dove.data.monad.onError
import com.dove.data.monad.onSuccess
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.util.*
import io.ktor.util.pipeline.*

class Authorization<TPrincipal : Any>(
    private val authorize: suspend PipelineContext<*, ApplicationCall>.() -> ApiResult<TPrincipal>,
    private val catch: suspend PipelineContext<*, ApplicationCall>.(ApiError) -> Unit,
    private val principalKey: AttributeKey<TPrincipal>
) {
    class Configuration<TPrincipal> {
        internal var authorize: (suspend PipelineContext<*, ApplicationCall>.() -> ApiResult<TPrincipal>)? = null
        fun authorize(block: suspend PipelineContext<*, ApplicationCall>.() -> ApiResult<TPrincipal>) {
            authorize = block
        }

        internal var catch: suspend PipelineContext<*, ApplicationCall>.(ApiError) -> Unit =
            {
                call.respond(HttpStatusCode.Unauthorized)
                finish()
            }

        fun catch(block: suspend PipelineContext<*, ApplicationCall>.(ApiError) -> Unit) {
            catch = block
        }
    }

    class Feature<TPrincipal : Any>(name: String = "AuthorizationFeature") :
        ApplicationFeature<ApplicationCallPipeline, Configuration<TPrincipal>, Authorization<TPrincipal>> {
        val principalKey = AttributeKey<TPrincipal>(name = "${name}AuthorizedPrincipal")

        override val key = AttributeKey<Authorization<TPrincipal>>(name)

        override fun install(
            pipeline: ApplicationCallPipeline,
            configure: Configuration<TPrincipal>.() -> Unit
        ): Authorization<TPrincipal> {
            val configuration = Configuration<TPrincipal>().apply(configure)
            return Authorization(
                authorize = configuration.authorize ?: error("Authorize action is not configured"),
                catch = configuration.catch,
                principalKey = principalKey
            )
        }
    }

    suspend fun authorizePipeline(context: PipelineContext<*, ApplicationCall>) {
        authorize(context).onError { throwable ->
            catch(context, throwable)
        }.onSuccess { principal ->
            context.context.attributes.put(principalKey, principal)
        }
    }
}