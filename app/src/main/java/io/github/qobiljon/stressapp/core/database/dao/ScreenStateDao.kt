package io.github.qobiljon.stressapp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.qobiljon.stressapp.core.database.data.ScreenState

@Dao
interface ScreenStateDao {
    @Query("SELECT * FROM screenstate;")
    fun getAll(): List<ScreenState>

    @Query("SELECT * FROM screenstate ORDER BY timestamp ASC LIMIT :k")
    fun getK(k: Int): List<ScreenState>

    @Insert
    fun insertAll(vararg screenState: ScreenState)

    @Delete
    fun delete(screenState: ScreenState)
}