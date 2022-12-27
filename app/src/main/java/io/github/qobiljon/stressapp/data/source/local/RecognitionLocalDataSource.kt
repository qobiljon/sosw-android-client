package io.github.qobiljon.stressapp.data.source.local

import io.github.qobiljon.stressapp.core.database.data.ActivityRecognition

interface RecognitionLocalDataSource {
    fun getActivityRecognitions(): List<ActivityRecognition>
    fun insertActivityRecognitions(activityRecognitions: List<ActivityRecognition>)
    fun deleteRecognition(activityRecognition: ActivityRecognition)
}