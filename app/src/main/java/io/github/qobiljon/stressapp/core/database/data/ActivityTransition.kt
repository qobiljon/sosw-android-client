package io.github.qobiljon.stressapp.core.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ActivityTransition(
    @PrimaryKey val timestamp: Long,
    @ColumnInfo(name = "activity") val activity: String,
    @ColumnInfo(name = "transition") val transition: String,
)
