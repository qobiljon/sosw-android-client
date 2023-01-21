package io.github.qobiljon.stressapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
import io.github.qobiljon.stressapp.core.database.DatabaseHelper
import io.github.qobiljon.stressapp.ui.MainActivity


class ActivityTransitionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        if (ActivityTransitionResult.hasResult(intent)) ActivityTransitionResult.extractResult(intent)?.let { result ->
            for (event in result.transitionEvents) {
                val activity = parseActivityType(event.activityType)
                val transition = parseTransitionType(event.transitionType)

                Log.e(MainActivity.TAG, "Activity transition: $activity, $transition")

                // It will change in the future
                DatabaseHelper.saveActivityTransition(
                    listOf(
                        io.github.qobiljon.stressapp.core.database.data.ActivityTransition(
                            timestamp = System.currentTimeMillis(),
                            activity = activity,
                            transition = transition,
                        )
                    )

                )
            }
        }
    }

    private fun parseActivityType(activity: Int?): String {
        return when (activity) {
            DetectedActivity.IN_VEHICLE -> "IN_VEHICLE"
            DetectedActivity.ON_BICYCLE -> "ON_BICYCLE"
            DetectedActivity.ON_FOOT -> "ON_FOOT"
            DetectedActivity.STILL -> "STILL"
            DetectedActivity.WALKING -> "WALKING"
            DetectedActivity.RUNNING -> "RUNNING"
            else -> "UNKNOWN"
        }
    }

    private fun parseTransitionType(transitionType: Int?): String {
        return when (transitionType) {
            ActivityTransition.ACTIVITY_TRANSITION_ENTER -> "ENTER"
            ActivityTransition.ACTIVITY_TRANSITION_EXIT -> "EXIT"
            else -> "UNKNOWN"
        }
    }
}
