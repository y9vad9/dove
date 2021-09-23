package com.dove.backend.features.time

import java.util.*

val timeInMs: Long get() = Calendar.getInstance(TimeZone.getTimeZone("Europe/Kiev")).timeInMillis