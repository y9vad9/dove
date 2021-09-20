package com.dove.server.features.files

import com.dove.data.monad.valueOrNull
import com.dove.server.features.Tags
import com.dove.server.features.files.storage.FilesInfoStorage
import com.dove.server.features.files.storage.FilesStorage
import com.dove.server.utils.openapi.getNullable
import com.dove.server.utils.openapi.post
import com.dove.server.utils.openapi.user
import com.dove.server.utils.openapi.userAuthorized
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.content.type.binary.BinaryRequest
import com.papsign.ktor.openapigen.content.type.binary.BinaryResponse
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.route
import com.papsign.ktor.openapigen.route.tag
import java.io.InputStream

private val api: FilesAPI = FilesAPI(FilesInfoStorage.Default, FilesStorage.Default)

fun NormalOpenAPIRoute.files() = tag(Tags.Files).route("/files") {
    uploadFileRequest()
    getFileContentRequest()
}

@BinaryRequest(["*/*"])
private data class UploadFileRequest(
    val stream: InputStream
)

private data class UploadFileParameters(
    @QueryParam("name of file")
    val name: String
)

private fun NormalOpenAPIRoute.uploadFileRequest() = userAuthorized {
    post<UploadFileParameters, String, UploadFileRequest> {
        api.upload(user, name, it.stream)
    }
}

private data class GetFileContentRequest(
    @QueryParam("File UUID")
    val fileUUID: String
)

@BinaryResponse(["*/*"])
private data class GetFileContentResponse(
    val stream: InputStream
)

private fun NormalOpenAPIRoute.getFileContentRequest() = getNullable<GetFileContentRequest, GetFileContentResponse> {
    GetFileContentResponse(
        api.getFileBytes(fileUUID).valueOrNull() ?: return@getNullable null
    )
}