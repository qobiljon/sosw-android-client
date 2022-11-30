package io.github.qobiljon.stressapp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.qobiljon.stressapp.core.database.data.ActivityTransition

@Dao
interface ActivityTransitionDao {
    @Query("SELECT * FROM activitytransition;")
    fun getAll(): List<ActivityTransition>

    @Insert
    fun insertAll(vararg activityTransition: ActivityTransition)

    @Delete
    fun delete(activityTransition: ActivityTransition)
}