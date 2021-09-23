package com.dove.backend.server.events

import com.dove.data.api.events.Event
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

private val events: MutableSharedFlow<Event> = MutableSharedFlow()

object EventsDispatcher : SharedFlow<Event> by events {
    /**
     * Sends new event to listeners.
     * @param event - event to send.
     */
    suspend fun sendEvent(event: Event): Unit = events.emit(event)
}