package com.dove.annotations

/**
 * Annotates that field cannot be less than [min].
 */
@OptIn(ExperimentalMultiplatform::class)
@Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY)
@OptionalExpectation
expect annotation class MinLength(val value: Int, val errorMessage: String = "")