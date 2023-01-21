package io.github.qobiljon.stressapp.data.source.local

import io.github.qobiljon.stressapp.core.database.dao.ActivityRecognitionDao
import io.github.qobiljon.stressapp.core.database.data.ActivityRecognition
import javax.inject.Inject

class DefaultRecognitionLocalDataSource @Inject constructor(
    private val recognitionDao: ActivityRecognitionDao
) : RecognitionLocalDataSource {

    override fun getActivityRecognitions(): List<ActivityRecognition> {
        return recognitionDao.getAll()
    }

    override fun insertActivityRecognitions(activityRecognitions: List<ActivityRecognition>) {
        return recognitionDao.insertAll(activityRecognitions)
    }

    override fun deleteRecognition(activityRecognition: ActivityRecognition) {
        return recognitionDao.delete(activityRecognition)
    }
}