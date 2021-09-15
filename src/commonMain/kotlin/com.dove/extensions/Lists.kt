package com.dove.extensions

fun <E> List<E>.limit(range: IntRange): List<E> {
    return drop(range.first).take(range.last - range.first)
}

