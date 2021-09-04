package com.dove.data.chats.messages

data class FileInfo(
    val uuid: String,
    val originalFileName: String,
    val fileHash: String,
    val fileOwnerId: Long,
    val uploadTime: Long
)