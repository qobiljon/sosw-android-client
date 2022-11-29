package io.github.qobiljon.stressapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
import io.github.qobiljon.stressapp.BuildConfig


class DetectedActivityReceiver : BroadcastReceiver() {
    companion object {
        val RECEIVER_ACTION = BuildConfig.APPLICATION_ID + ".DetectedActivityReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent) {

        if (RECEIVER_ACTION == intent.action) {
            Log.d("DetectedActivityReceiver", "Received an unsupported action.")
            return
        }

        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent)
            for (event in result!!.transitionEvents) {
                val activity = activityType(event.activityType).toString()
                val transition = transitionType(event.transitionType).toString()
                val message = "Transition: $activity ($transition)"
                Log.d("DetectedActivityReceiver", message)
            }
        }
    }

    private fun transitionType(transitionType: Int): String? {
        return when (transitionType) {
            ActivityTransition.ACTIVITY_TRANSITION_ENTER -> "ENTER"
            ActivityTransition.ACTIVITY_TRANSITION_EXIT -> "EXIT"
            else -> "UNKNOWN"
        }
    }

    private fun activityType(activity: Int): String? {
        return when (activity) {
            DetectedActivity.IN_VEHICLE -> "IN_VEHICLE"
            DetectedActivity.STILL -> "STILL"
            DetectedActivity.WALKING -> "WALKING"
            else -> "UNKNOWN"
        }
    }

//    override fun onReceive(context: Context, intent: Intent) {
//        if (ActivityTransitionResult.hasResult(intent)) {
//            val result = ActivityTransitionResult.extractResult(intent)
//            result?.let {
//                result.transitionEvents.forEach { event ->
//                    val timestamp = System.currentTimeMillis() + (event.elapsedRealTimeNanos - System.nanoTime()) / 1000000L;
//                    val activityType = when (event.activityType) {
//                        DetectedActivity.STILL -> "STILL"
//                        DetectedActivity.WALKING -> "WALKING"
//                        DetectedActivity.IN_VEHICLE -> "IN VEHICLE"
//                        DetectedActivity.RUNNING -> "RUNNING"
//                        DetectedActivity.ON_FOOT -> "RUNNING"
//                        DetectedActivity.ON_BICYCLE -> "RUNNING"
//                        DetectedActivity.TILTING -> "RUNNING"
//                        else -> "N/A"
//                    }
//                    val transitionType = when (event.transitionType) {
//                        ActivityTransition.ACTIVITY_TRANSITION_ENTER -> "ENTER"
//                        ActivityTransition.ACTIVITY_TRANSITION_EXIT -> "EXIT"
//                        else -> "N/A"
//                    }
//                    Log.e(MainActivity.TAG, "$timestamp, $activityType, $transitionType")
//
//                    DatabaseHelper.saveActivityTransition(
//                        io.github.qobiljon.stressapp.core.database.data.ActivityTransition(
//                            timestamp = timestamp,
//                            activity_type = activityType,
//                            transition_type = transitionType,
//                        )
//                    )
//                }
//            }
//        }
//    }
}
