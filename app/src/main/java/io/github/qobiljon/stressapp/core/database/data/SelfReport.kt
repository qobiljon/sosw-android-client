package io.github.qobiljon.stressapp.core.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SelfReport(
    @PrimaryKey val timestamp: Long,
    @ColumnInfo(name = "pss_control") val pss_control: Int,
    @ColumnInfo(name = "pss_confident") val pss_confident: Int,
    @ColumnInfo(name = "pss_yourway") val pss_yourway: Int,
    @ColumnInfo(name = "pss_difficulties") val pss_difficulties: Int,
    @ColumnInfo(name = "stresslvl") val stresslvl: Int,
    @ColumnInfo(name = "social_settings") val social_settings: String,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "activity") val activity: String,
)