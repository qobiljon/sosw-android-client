package io.github.qobiljon.stressapp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.qobiljon.stressapp.core.database.data.Location

@Dao
interface LocationDao {
    @Query("SELECT * FROM location;")
    fun getAll(): List<Location>

    @Query("SELECT EXISTS(SELECT 1 FROM location WHERE timestamp = :timestamp)")
    fun exists(timestamp: Long): Boolean

    @Insert
    fun insertAll(locations: List<Location>)

    @Delete
    fun delete(location: Location)
}