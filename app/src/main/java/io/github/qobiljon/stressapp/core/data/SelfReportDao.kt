package io.github.qobiljon.stressapp.core.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SelfReportDao {
    @Query("SELECT * FROM selfreport;")
    fun getAll(): List<SelfReport>

    @Query("SELECT * FROM selfreport WHERE is_submitted = :isSubmitted ORDER BY timestamp ASC")
    fun getFiltered(isSubmitted: Boolean): List<SelfReport>

    @Query("SELECT * FROM selfreport ORDER BY timestamp ASC LIMIT :k")
    fun getK(k: Int): List<SelfReport>

    @Query("UPDATE selfreport SET is_submitted = :isSubmitted WHERE timestamp = :timestamp")
    fun setIsSubmitted(timestamp: Long, isSubmitted: Boolean)

    @Insert
    fun insertAll(vararg selfReports: SelfReport)

    @Delete
    fun delete(user: SelfReport)
}