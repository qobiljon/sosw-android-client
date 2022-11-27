package io.github.qobiljon.stressapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.qobiljon.stressapp.core.database.dao.CalendarEventDao
import io.github.qobiljon.stressapp.core.database.dao.SelfReportDao
import io.github.qobiljon.stressapp.core.database.data.CalendarEvent
import io.github.qobiljon.stressapp.core.database.data.SelfReport

@Database(
    entities = [
        SelfReport::class,
        CalendarEvent::class,
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun selfReportDao(): SelfReportDao
    abstract fun calendarEventDao(): CalendarEventDao
}