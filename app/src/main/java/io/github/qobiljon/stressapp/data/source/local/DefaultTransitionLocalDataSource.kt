package io.github.qobiljon.stressapp.data.source.local

import io.github.qobiljon.stressapp.core.database.dao.ActivityTransitionDao
import io.github.qobiljon.stressapp.core.database.data.ActivityTransition
import javax.inject.Inject

class DefaultTransitionLocalDataSource @Inject constructor(
    private val activityTransitionDao: ActivityTransitionDao
): TransitionLocalDataSource {

    override fun getActivityTransitions(): List<ActivityTransition> {
        return activityTransitionDao.getAll()
    }

    override fun insertActivityTransitions(activityTransitions: List<ActivityTransition>) {
        return activityTransitionDao.insertAll(activityTransitions)
    }

    override fun deleteActivityTransition(activityTransition: ActivityTransition) {
        return activityTransitionDao.delete(activityTransition)
    }
}