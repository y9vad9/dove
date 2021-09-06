package com.dove.server.features.files

import com.dove.data.monad.valueOrNull
import com.dove.server.features.Tags
import com.dove.server.features.users.tokens.AuthorizeByTokenRequest
import com.dove.server.utils.openapi.getNullable
import com.dove.server.utils.openapi.post
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.content.type.binary.BinaryResponse
import com.papsign.ktor.openapigen.content.type.multipart.FormDataRequest
import com.papsign.ktor.openapigen.content.type.multipart.NamedFileInputStream
import com.papsign.ktor.openapigen.content.type.multipart.PartEncoding
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.tag
import java.io.InputStream

fun NormalOpenAPIRoute.files() = tag(Tags.Files) {
    uploadFileRequest()
    getFileContentRequest()
}

@FormDataRequest
private data class UploadFileRequest(
    @PartEncoding("*/*")
    val file: NamedFileInputStream,
    val name: String
)

private fun NormalOpenAPIRoute.uploadFileRequest() = post<UploadFileRequest, String, AuthorizeByTokenRequest> {
    FilesAPI.upload(it.token, name, file)
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
        FilesAPI.getFileBytes(fileUUID).valueOrNull() ?: return@getNullable null
    )
}