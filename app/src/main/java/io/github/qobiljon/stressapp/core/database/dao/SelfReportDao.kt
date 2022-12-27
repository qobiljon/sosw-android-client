package io.github.qobiljon.stressapp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.qobiljon.stressapp.core.database.data.SelfReport

@Dao
interface SelfReportDao {
    @Query("SELECT * FROM selfreport")
    fun getAll(): List<SelfReport>

    @Insert
    fun insertAll(selfReports: List<SelfReport>)

    @Delete
    fun delete(selfReport: SelfReport)
}