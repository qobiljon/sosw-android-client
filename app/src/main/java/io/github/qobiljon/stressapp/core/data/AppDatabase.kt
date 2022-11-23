package io.github.qobiljon.stressapp.core.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SelfReport::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun selfReportDao(): SelfReportDao
}