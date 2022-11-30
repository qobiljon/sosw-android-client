package io.github.qobiljon.stressapp.core.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CallLog(
    @PrimaryKey val timestamp: Long,
    @ColumnInfo(name = "number") val number: String,
    @ColumnInfo(name = "duration") val duration: String,
    @ColumnInfo(name = "call_type") val call_type: String,
    @ColumnInfo(name = "submitted") val submitted: Boolean,
)
