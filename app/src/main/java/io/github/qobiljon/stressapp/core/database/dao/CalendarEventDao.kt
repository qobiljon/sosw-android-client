package io.github.qobiljon.stressapp.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.qobiljon.stressapp.core.database.data.CalendarEvent

@Dao
interface CalendarEventDao {
    @Query("SELECT * FROM calendarevent WHERE submitted = :submitted")
    fun getFiltered(submitted: Boolean): List<CalendarEvent>

    @Query("SELECT exists(SELECT 1 FROM calendarevent WHERE event_id = :eventId)")
    fun exists(eventId: String): Boolean

    @Query("UPDATE calendarevent set submitted = :submitted WHERE event_id = :eventId")
    fun setSubmitted(submitted: Boolean, eventId: String)

    @Insert
    fun insertAll(calendarEvents: List<CalendarEvent>)
}