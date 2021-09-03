package com.dove.annotations

@OptIn(ExperimentalMultiplatform::class)
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@OptionalExpectation
expect annotation class MaxLength(val value: Int, val errorMessage: String = "")
