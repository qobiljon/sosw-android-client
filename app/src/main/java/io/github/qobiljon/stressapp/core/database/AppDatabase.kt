package io.github.qobiljon.stressapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.qobiljon.stressapp.core.database.dao.*
import io.github.qobiljon.stressapp.core.database.data.*

@Database(
    entities = [
        SelfReport::class,
        CalendarEvent::class,
        Location::class,
        ScreenState::class,
        ActivityTransition::class,
        ActivityRecognition::class,
        CallLog::class,
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun selfReportDao(): SelfReportDao
    abstract fun calendarEventDao(): CalendarEventDao
    abstract fun locationDao(): LocationDao
    abstract fun screenStateDao(): ScreenStateDao
    abstract fun activityTransitionDao(): ActivityTransitionDao
    abstract fun activityRecognitionDao(): ActivityRecognitionDao
    abstract fun callLogDao(): CallLogDao
}
