package io.github.qobiljon.stressapp.core.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SelfReportDao {
    @Query("SELECT * FROM selfreport;")
    fun getAll(): List<SelfReport>

    @Insert
    fun insertAll(vararg users: SelfReport)

    @Delete
    fun delete(user: SelfReport)
}