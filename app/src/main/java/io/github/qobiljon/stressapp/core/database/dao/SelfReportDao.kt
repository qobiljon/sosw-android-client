package io.github.qobiljon.stressapp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.qobiljon.stressapp.core.database.data.SelfReport

@Dao
interface SelfReportDao {
    @Query("SELECT * FROM selfreport;")
    fun getAll(): List<SelfReport>

    @Query("SELECT * FROM selfreport ORDER BY timestamp ASC LIMIT :k")
    fun getK(k: Int): List<SelfReport>

    @Insert
    fun insertAll(vararg selfReports: SelfReport)

    @Delete
    fun delete(selfReport: SelfReport)
}