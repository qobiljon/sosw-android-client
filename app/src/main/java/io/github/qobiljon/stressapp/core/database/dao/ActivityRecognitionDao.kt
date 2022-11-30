package io.github.qobiljon.stressapp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.qobiljon.stressapp.core.database.data.ActivityRecognition

@Dao
interface ActivityRecognitionDao {
    @Query("SELECT * FROM activityrecognition;")
    fun getAll(): List<ActivityRecognition>

    @Insert
    fun insertAll(vararg activityRecognition: ActivityRecognition)

    @Delete
    fun delete(activityRecognition: ActivityRecognition)
}