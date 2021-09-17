package com.dove.server.utils.hashing

import java.io.InputStream
import java.math.BigInteger
import java.security.DigestInputStream
import java.security.MessageDigest

fun String.toMD5() = toByteArray().toMD5()

fun ByteArray.toMD5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(this)).toString(16).padStart(32, '0')
}

fun InputStream.toMD5(): String {
    val md = MessageDigest.getInstance("MD5")
    use {
        DigestInputStream(it, md)
    }
    return String(md.digest())
}