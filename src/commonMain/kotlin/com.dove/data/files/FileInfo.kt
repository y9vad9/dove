package com.dove.data.files

import kotlinx.serialization.Serializable

@Serializable
data class FileInfo(
    val uuid: String,
    val originalFileName: String,
    val fileHash: String,
    val fileOwnerId: Long,
    val uploadTime: Long
)