package com.dove.server.utils.hashing

import java.math.BigInteger
import java.security.MessageDigest

fun String.toMD5() = toByteArray().toMD5()

fun ByteArray.toMD5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(this)).toString(16).padStart(32, '0')
}