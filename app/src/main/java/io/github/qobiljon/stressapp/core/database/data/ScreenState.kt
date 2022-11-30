package io.github.qobiljon.stressapp.core.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScreenState(
    @PrimaryKey val timestamp: Long,
    @ColumnInfo(name = "screen_state") val screen_state: String,
    @ColumnInfo(name = "keyguard_restricted_input_mode") val keyguard_restricted_input_mode: Boolean,
)
