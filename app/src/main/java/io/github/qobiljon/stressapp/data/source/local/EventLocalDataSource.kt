package io.github.qobiljon.stressapp.data.source.local

import io.github.qobiljon.stressapp.core.database.data.CalendarEvent

interface EventLocalDataSource {
    fun getFilteredEvents(submitted: Boolean): List<CalendarEvent>
    fun hasEventExist(eventId: String): Boolean
    fun setSubmittedEvent(submitted: Boolean, eventId: String)
    fun insertEvents(calendarEvent: List<CalendarEvent>)
}