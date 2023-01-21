package io.github.qobiljon.stressapp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.qobiljon.stressapp.core.database.data.ActivityRecognition

@Dao
interface ActivityRecognitionDao {
    @Query("SELECT * FROM activity_recognition_table_name")
    fun getAll(): List<ActivityRecognition>

    @Insert
    fun insertAll(activityRecognitions: List<ActivityRecognition>)

    @Delete
    fun delete(activityRecognition: ActivityRecognition)
}