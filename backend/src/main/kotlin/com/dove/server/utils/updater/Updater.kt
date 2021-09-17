package com.dove.server.utils.updater

import com.dove.data.api.events.Event
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object Updater {
    private val eventsSource: MutableSharedFlow<Event> = MutableSharedFlow()

    /**
     * All updates in api are collecting here.
     */
    val events: SharedFlow<Event> = eventsSource.asSharedFlow()

    /**
     * Sends event to [events].
     */
    suspend fun sendEvent(event: Event) {
        eventsSource.emit(event)
    }
}