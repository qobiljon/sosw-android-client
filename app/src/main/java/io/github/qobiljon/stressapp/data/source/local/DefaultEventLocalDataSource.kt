package io.github.qobiljon.stressapp.data.source.local

import io.github.qobiljon.stressapp.core.database.dao.CalendarEventDao
import io.github.qobiljon.stressapp.core.database.data.CalendarEvent
import javax.inject.Inject

class DefaultEventLocalDataSource @Inject constructor(
    private val eventDao: CalendarEventDao
) : EventLocalDataSource {
    override fun getFilteredEvents(submitted: Boolean): List<CalendarEvent> {
        return eventDao.getFiltered(submitted)
    }

    override fun hasEventExist(eventId: String): Boolean {
        return eventDao.exists(eventId)
    }

    override fun setSubmittedEvent(submitted: Boolean, eventId: String) {
        return eventDao.setSubmitted(submitted, eventId)
    }

    override fun insertEvents(calendarEvent: List<CalendarEvent>) {
        return eventDao.insertAll(calendarEvent)
    }
}