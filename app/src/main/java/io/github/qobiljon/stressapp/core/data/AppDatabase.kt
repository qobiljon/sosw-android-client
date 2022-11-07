package io.github.qobiljon.stressapp.core.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        SelfReport::class,
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun selfReportDao(): SelfReportDao
}