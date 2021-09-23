package com.dove.backend.server.api.routings.files

import com.dove.backend.apis.files.FilesAPI
import com.dove.backend.server.api.routings.Tags
import com.dove.backend.server.api.routings.users.UsersAuthorizedScope
import com.dove.data.api.ApiError
import com.dove.data.monad.Either
import com.dove.data.monad.isSuccess
import com.dove.data.monad.map
import com.dove.data.users.User
import com.papsign.ktor.openapigen.annotations.Path
import com.papsign.ktor.openapigen.annotations.parameters.PathParam
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.content.type.binary.BinaryRequest
import com.papsign.ktor.openapigen.content.type.binary.BinaryResponse
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag
import com.y9neon.middleware.authorization.Authorization
import com.y9neon.openapi.get
import com.y9neon.openapi.post
import java.io.InputStream

fun NormalOpenAPIRoute.files(
    api: FilesAPI,
    feature: Authorization.Feature<User>
) = tag(Tags.Files).route("/files") {

    fun userAuthorized(block: UsersAuthorizedScope<NormalOpenAPIRoute>.() -> Unit) {
        block(UsersAuthorizedScope(this, feature))
    }

    @BinaryRequest(["*/*"])
    data class UploadFileRequest(
        val stream: InputStream
    )

    data class UploadFileParameters(
        @QueryParam("name of file")
        val name: String
    )

    fun NormalOpenAPIRoute.uploadFileRequest() = userAuthorized {
        post<UploadFileParameters, String, UploadFileRequest> {
            api.upload(user, name, it.stream)
        }
    }

    @Path("/{fileUUID}")
    data class GetFileContentRequest(
        @PathParam("File UUID")
        val fileUUID: String
    )

    @BinaryResponse(["*/*"])
    data class GetFileContentResponse(
        val stream: InputStream
    )

    fun NormalOpenAPIRoute.getFileContentRequest() = get<GetFileContentRequest, GetFileContentResponse> {
        api.getFileBytes(fileUUID)
            .map {
                if (it.isSuccess())
                    Either.success(GetFileContentResponse(it.value))
                else Either.error(ApiError.FileNotFoundError)
            }
    }

    uploadFileRequest()
    getFileContentRequest()
}