package io.github.qobiljon.stressapp.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.provider.CalendarContract
import android.provider.CallLog
import android.util.Log
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.core.database.DatabaseHelper
import io.github.qobiljon.stressapp.core.database.data.CalendarEvent
import io.github.qobiljon.stressapp.core.database.data.Location
import io.github.qobiljon.stressapp.receivers.ActivityRecognitionReceiver
import io.github.qobiljon.stressapp.receivers.ActivityTransitionReceiver
import io.github.qobiljon.stressapp.receivers.ScreenStateReceiver
import io.github.qobiljon.stressapp.ui.MainActivity
import java.time.Instant
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class DataCollectionService : Service() {
    companion object {
        private const val CALLS_CALENDAR_SAMPLING_INTERVAL_MS = 60 * 60 * 1000L
        private const val ACTIVITY_RECOGNITION_INTERVAL_MS = 10 * 1000L
        private const val LOCATION_INTERVAL_MS = 60 * 1000L
        var isRunning = false
    }

    private val mBinder: IBinder = LocalBinder()
    private val executor = Executors.newScheduledThreadPool(10)

    inner class LocalBinder : Binder() {
        @Suppress("unused")
        val getService: DataCollectionService
            get() = this@DataCollectionService
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    override fun onCreate() {
        Log.e(MainActivity.TAG, "DataCollectionService.onCreate()")

        // foreground svc
        val notificationId = 98764
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        val notificationChannelId = javaClass.name
        val notificationChannelName = "Motion and HR data collection"
        val notificationChannel = NotificationChannel(notificationChannelId, notificationChannelName, NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        val notification = Notification.Builder(this, notificationChannelId).setContentTitle(getString(R.string.app_name)).setContentText("Context sensing service running").setSmallIcon(R.mipmap.ic_stress_app).setContentIntent(pendingIntent).build()
        startForeground(notificationId, notification)

        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e(MainActivity.TAG, "DataCollectionService.onStartCommand()")
        if (isRunning) return START_STICKY
        else {
            isRunning = true

            executor.scheduleAtFixedRate({
                try {
                    getNewCalendarEvents() // calendar data
                } catch (_: RuntimeException) {
                }

                try {
                    getNewCalls() // call logs
                } catch (_: RuntimeException) {
                }
            }, 0L, CALLS_CALENDAR_SAMPLING_INTERVAL_MS, TimeUnit.MILLISECONDS)

            setUpLocationListener() // location listener
            setUpScreenStateReceiver() // screen state listener
            setUpActivityTransition() // activity transition updates
            setUpActivityRecognition() // activity recognition
        }
        return START_STICKY
    }

    override fun onDestroy() {
        Log.e(MainActivity.TAG, "DataCollectionService.onDestroy()")

        isRunning = false

        super.onDestroy()
    }

    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        // last known location
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        fusedLocationClient.lastLocation.addOnSuccessListener { l ->
            if (l != null) DatabaseHelper.saveLocation(

                listOf( Location(
                    timestamp = l.time,
                    latitude = l.latitude,
                    longitude = l.longitude,
                    accuracy = l.accuracy,
                ))
            )
        }

        // location updates
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = LOCATION_INTERVAL_MS
            fastestInterval = 10
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = LOCATION_INTERVAL_MS
        }
        val locationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        locationProviderClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach { l ->
                    if (l != null) {
                        DatabaseHelper.saveLocation(
                            listOf(Location(
                                timestamp = l.time,
                                latitude = l.latitude,
                                longitude = l.longitude,
                                accuracy = l.accuracy,
                            ))
                        )
                    }
                }
            }
        }, Looper.getMainLooper())
    }

    private fun setUpScreenStateReceiver() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_USER_PRESENT)
        applicationContext.registerReceiver(ScreenStateReceiver(), filter)
    }

    @SuppressLint("UnspecifiedImmutableFlag", "MissingPermission")
    private fun setUpActivityTransition() {
        run {
            val request = ActivityTransitionRequest(
                listOf(
                    ActivityTransition.Builder().setActivityType(DetectedActivity.IN_VEHICLE).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build(),
                    ActivityTransition.Builder().setActivityType(DetectedActivity.IN_VEHICLE).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build(),
                    ActivityTransition.Builder().setActivityType(DetectedActivity.ON_BICYCLE).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build(),
                    ActivityTransition.Builder().setActivityType(DetectedActivity.ON_BICYCLE).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build(),
                    ActivityTransition.Builder().setActivityType(DetectedActivity.ON_FOOT).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build(),
                    ActivityTransition.Builder().setActivityType(DetectedActivity.ON_FOOT).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build(),
                    ActivityTransition.Builder().setActivityType(DetectedActivity.STILL).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build(),
                    ActivityTransition.Builder().setActivityType(DetectedActivity.STILL).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build(),
                    ActivityTransition.Builder().setActivityType(DetectedActivity.WALKING).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build(),
                    ActivityTransition.Builder().setActivityType(DetectedActivity.WALKING).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build(),
                    ActivityTransition.Builder().setActivityType(DetectedActivity.RUNNING).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build(),
                    ActivityTransition.Builder().setActivityType(DetectedActivity.RUNNING).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build(),
                )
            )
            val intent = Intent(applicationContext, ActivityTransitionReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(applicationContext, 7, intent, PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) 0 else PendingIntent.FLAG_MUTABLE)
            val client = ActivityRecognition.getClient(applicationContext)
            val task = client.requestActivityTransitionUpdates(request, pendingIntent)
            task.addOnSuccessListener { Log.e(MainActivity.TAG, "Activity transition listener configured successfully") }
            task.addOnFailureListener { Log.e(MainActivity.TAG, "Failed to configure activity transition listener") }
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag", "MissingPermission")
    private fun setUpActivityRecognition() {
        val intent = Intent(applicationContext, ActivityRecognitionReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 8, intent, PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) 0 else PendingIntent.FLAG_MUTABLE)
        val client = ActivityRecognition.getClient(applicationContext)
        val task = client.requestActivityUpdates(ACTIVITY_RECOGNITION_INTERVAL_MS, pendingIntent)
        task.addOnSuccessListener { Log.e(MainActivity.TAG, "Activity recognition listener configured successfully") }
        task.addOnFailureListener { Log.e(MainActivity.TAG, "Failed to configure activity recognition listener") }
    }

    @Suppress("SENSELESS_COMPARISON")
    private fun getNewCalendarEvents() {
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = 2022
        cal[Calendar.MONTH] = 0
        cal[Calendar.DAY_OF_MONTH] = 1
        val tsRangeFrom = cal.timeInMillis
        cal[Calendar.YEAR] = 2024
        val tsRangeUntil = cal.timeInMillis

        contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            arrayOf(
                CalendarContract.Events.ORIGINAL_ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
                CalendarContract.Events.EVENT_LOCATION,
            ),
            "(${CalendarContract.Events.DELETED} != 1) AND (${CalendarContract.Events.DTSTART} > $tsRangeFrom) AND (${CalendarContract.Events.DTEND} < $tsRangeUntil)",
            null,
            null,
        )?.use {
            val dao = DatabaseHelper.db.calendarEventDao()

            val originalIdIdx = it.getColumnIndex(CalendarContract.Events.ORIGINAL_ID)
            val titleIdx = it.getColumnIndex(CalendarContract.Events.TITLE)
            val startDateIdx = it.getColumnIndex(CalendarContract.Events.DTSTART)
            val endDateIdx = it.getColumnIndex(CalendarContract.Events.DTEND)
            val eventLocationIdx = it.getColumnIndex(CalendarContract.Events.EVENT_LOCATION)

            if (it.moveToFirst()) do {
                val title = it.getString(titleIdx)
                val startTs = it.getLong(startDateIdx)
                val eventId = "${it.getString(originalIdIdx)}_${startTs}"
                val endTs = it.getLong(endDateIdx)
                val eventLocation = it.getString(eventLocationIdx) ?: ""
                val event =  CalendarEvent(
                    event_id = eventId,
                    title = title,
                    start_ts = startTs,
                    end_ts = endTs,
                    event_location = eventLocation,
                    submitted = false,
                )

                if (title != null && startTs != null && endTs != null)
                    if (!dao.exists(eventId = eventId)) dao.insertAll(
                       listOf(event)
                    )
            } while (it.moveToNext())
        }
    }

    private fun getNewCalls() {
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = 2022
        cal[Calendar.MONTH] = 0
        cal[Calendar.DAY_OF_MONTH] = 1
        val tsRangeFrom = Date.from(Instant.ofEpochMilli(cal.timeInMillis)).time
        cal[Calendar.YEAR] = 2024
        val tsRangeUntil = Date.from(Instant.ofEpochMilli(cal.timeInMillis)).time

        baseContext.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            arrayOf(
                CallLog.Calls.DATE,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE,
            ),
            "(${CallLog.Calls.DATE} > $tsRangeFrom) AND (${CallLog.Calls.DATE} < $tsRangeUntil)",
            null,
            null,
        )?.use {
            if (it.moveToFirst()) do {
                val callDate = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.DATE))
                val number = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                val duration = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.DURATION))
                val callType = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.TYPE))
                if (callDate != null && number != null && duration != null && callType != null) {
                    val timestamp = Date(java.lang.Long.valueOf(callDate)).time
                    val _callType = when (callType.toInt()) {
                        CallLog.Calls.OUTGOING_TYPE -> "OUTGOING"
                        CallLog.Calls.INCOMING_TYPE -> "INCOMING"
                        CallLog.Calls.MISSED_TYPE -> "MISSED"
                        else -> "UNKNOWN"
                    }

                    DatabaseHelper.saveCallLog(
                       listOf( io.github.qobiljon.stressapp.core.database.data.CallLog(
                           timestamp = timestamp,
                           number = number,
                           duration = duration,
                           call_type = _callType,
                           submitted = false,
                       ))
                    )
                }
            } while (it.moveToNext())
        }
    }
}
