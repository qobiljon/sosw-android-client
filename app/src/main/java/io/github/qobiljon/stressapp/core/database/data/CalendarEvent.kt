package io.github.qobiljon.stressapp.core.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CalendarEvent(
    @PrimaryKey val event_id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "start_ts") val start_ts: Long,
    @ColumnInfo(name = "end_ts") val end_ts: Long,
    @ColumnInfo(name = "event_location") val event_location: String?,
    @ColumnInfo(name = "submitted") val submitted: Boolean,
)
