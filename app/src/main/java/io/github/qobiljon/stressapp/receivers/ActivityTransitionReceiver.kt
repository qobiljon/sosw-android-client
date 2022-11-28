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
    override fun onReceive(context: Context, intent: Intent) {
        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent)
            result?.let {
                result.transitionEvents.forEach { event ->
                    val timestamp = System.currentTimeMillis() + (event.elapsedRealTimeNanos - System.nanoTime()) / 1000000L;
                    val activityType = when (event.activityType) {
                        DetectedActivity.STILL -> "STILL"
                        DetectedActivity.WALKING -> "WALKING"
                        DetectedActivity.IN_VEHICLE -> "IN VEHICLE"
                        DetectedActivity.RUNNING -> "RUNNING"
                        DetectedActivity.ON_FOOT -> "RUNNING"
                        DetectedActivity.ON_BICYCLE -> "RUNNING"
                        DetectedActivity.TILTING -> "RUNNING"
                        else -> "N/A"
                    }
                    val transitionType = when (event.transitionType) {
                        ActivityTransition.ACTIVITY_TRANSITION_ENTER -> "ENTER"
                        ActivityTransition.ACTIVITY_TRANSITION_EXIT -> "EXIT"
                        else -> "N/A"
                    }
                    Log.e(MainActivity.TAG, "$timestamp, $activityType, $transitionType")

                    DatabaseHelper.saveActivityTransition(
                        io.github.qobiljon.stressapp.core.database.data.ActivityTransition(
                            timestamp = timestamp,
                            activity_type = activityType,
                            transition_type = transitionType,
                        )
                    )
                }
            }
        }
    }
}
