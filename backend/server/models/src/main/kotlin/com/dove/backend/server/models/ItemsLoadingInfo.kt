package com.dove.backend.server.models

import com.papsign.ktor.openapigen.annotations.parameters.QueryParam

data class ItemsLoadingInfo(
    @QueryParam("Number to load")
    val number: Int,
    @QueryParam("List offset")
    val offset: Long
)