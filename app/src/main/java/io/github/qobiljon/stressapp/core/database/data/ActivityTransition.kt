package io.github.qobiljon.stressapp.core.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ActivityTransition(
    @PrimaryKey val timestamp: Long,
    @ColumnInfo(name = "activityType") val activity_type: String,
    @ColumnInfo(name = "transitionType") val transition_type: String,
)
