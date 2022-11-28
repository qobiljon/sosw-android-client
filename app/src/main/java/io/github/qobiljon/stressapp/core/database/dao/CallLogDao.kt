package io.github.qobiljon.stressapp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.qobiljon.stressapp.core.database.data.CallLog

@Dao
interface CallLogDao {
    @Query("SELECT * FROM calllog;")
    fun getAll(): List<CallLog>

    @Query("SELECT * FROM calllog ORDER BY timestamp ASC LIMIT :k")
    fun getK(k: Int): List<CallLog>

    @Query("SELECT * FROM calllog WHERE submitted = :submitted ORDER BY timestamp")
    fun getFiltered(submitted: Boolean): List<CallLog>

    @Query("SELECT EXISTS(SELECT 1 FROM calllog WHERE timestamp = :timestamp)")
    fun exists(timestamp: Long): Boolean

    @Query("UPDATE calllog set submitted = :submitted WHERE timestamp = :timestamp")
    fun setSubmitted(submitted: Boolean, timestamp: Long)

    @Insert
    fun insertAll(vararg callLog: CallLog)

    @Delete
    fun delete(callLog: CallLog)
}