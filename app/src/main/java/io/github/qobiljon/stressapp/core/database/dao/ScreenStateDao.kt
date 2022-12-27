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

    @Insert
    fun insertAll(screenStates: List<ScreenState>)

    @Delete
    fun delete(screenState: ScreenState)
}