package io.github.qobiljon.stressapp.data.source.local

import io.github.qobiljon.stressapp.core.database.data.ActivityTransition

interface TransitionLocalDataSource {
    fun getActivityTransitions(): List<ActivityTransition>
    fun insertActivityTransitions(activityTransitions: List<ActivityTransition>)
    fun deleteActivityTransition(activityTransition: ActivityTransition)
}