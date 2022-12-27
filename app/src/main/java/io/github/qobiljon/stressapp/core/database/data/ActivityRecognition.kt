package io.github.qobiljon.stressapp.core.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_recognition_table_name")
data class ActivityRecognition(
    @PrimaryKey val timestamp: Long,
    @ColumnInfo(name = "activity") val activity: String,
    @ColumnInfo(name = "confidence") val confidence: Int,
)
