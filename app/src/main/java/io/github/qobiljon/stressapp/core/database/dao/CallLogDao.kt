package io.github.qobiljon.stressapp.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.qobiljon.stressapp.core.database.data.CallLog

@Dao
interface CallLogDao {
    @Query("SELECT * FROM calllog WHERE submitted = :submitted ORDER BY timestamp")
    fun getFiltered(submitted: Boolean): List<CallLog>

    @Query("SELECT EXISTS(SELECT 1 FROM calllog WHERE timestamp = :timestamp)")
    fun exists(timestamp: Long): Boolean

    @Query("UPDATE calllog set submitted = :submitted WHERE timestamp = :timestamp")
    fun setSubmitted(submitted: Boolean, timestamp: Long)

    @Insert
    fun insertAll(vararg callLog: CallLog)
}