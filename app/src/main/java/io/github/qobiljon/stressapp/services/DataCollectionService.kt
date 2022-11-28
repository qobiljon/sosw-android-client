package io.github.qobiljon.stressapp.services

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import android.provider.CalendarContract
import android.provider.CallLog
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.core.database.DatabaseHelper
import io.github.qobiljon.stressapp.core.database.data.CalendarEvent
import io.github.qobiljon.stressapp.core.database.data.Location
import io.github.qobiljon.stressapp.receivers.ActivityTransitionReceiver
import io.github.qobiljon.stressapp.receivers.ScreenStateReceiver
import io.github.qobiljon.stressapp.ui.MainActivity
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class DataCollectionService : Service() {
    companion object {
        private const val DATA_SAMPLING_INTERVAL = 60L
    }

    var isRunning = false
    private val mBinder: IBinder = LocalBinder()
    private val executor = Executors.newScheduledThreadPool(10)
    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.forEach { l ->
                if (l != null) {
                    Log.e(MainActivity.TAG, "${l.time}, ${l.latitude}, ${l.longitude}, ${l.accuracy}")
                    DatabaseHelper.saveLocation(
                        Location(
                            timestamp = l.time,
                            latitude = l.latitude,
                            longitude = l.longitude,
                            accuracy = l.accuracy,
                        )
                    )
                }
            }
        }
    }

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

        setUpLocationListener() // location listener
        setUpScreenStateReceiver() // screen state listener
        setUpActivityTransition() // activity recognition

        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e(MainActivity.TAG, "DataCollectionService.onStartCommand()")
        if (isRunning) return START_STICKY
        else {
            executor.scheduleAtFixedRate({ sampleContextData() }, 0L, DataCollectionService.DATA_SAMPLING_INTERVAL, TimeUnit.SECONDS)
            isRunning = true
        }
        return START_STICKY
    }

    override fun onDestroy() {
        Log.e(MainActivity.TAG, "DataCollectionService.onDestroy()")

        isRunning = false

        super.onDestroy()
    }

    private fun setUpLocationListener() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        val missingPermissions = permissions.filter { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }
        if (missingPermissions.isEmpty()) fusedLocationClient.lastLocation.addOnSuccessListener { l ->
            if (l != null) DatabaseHelper.saveLocation(
                Location(
                    timestamp = l.time,
                    latitude = l.latitude,
                    longitude = l.longitude,
                    accuracy = l.accuracy,
                )
            )
        }
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 60000
            fastestInterval = 10
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 60000
        }
        val locationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun setUpScreenStateReceiver() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_USER_PRESENT)
        applicationContext.registerReceiver(ScreenStateReceiver(), filter)
    }

    private fun setUpActivityTransition() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) return

        val request = ActivityTransitionRequest(
            listOf(
                ActivityTransition.Builder().setActivityType(DetectedActivity.STILL).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build(),
                ActivityTransition.Builder().setActivityType(DetectedActivity.STILL).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build(),
                ActivityTransition.Builder().setActivityType(DetectedActivity.WALKING).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build(),
                ActivityTransition.Builder().setActivityType(DetectedActivity.WALKING).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build(),
                ActivityTransition.Builder().setActivityType(DetectedActivity.IN_VEHICLE).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build(),
                ActivityTransition.Builder().setActivityType(DetectedActivity.IN_VEHICLE).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build(),
                ActivityTransition.Builder().setActivityType(DetectedActivity.ON_FOOT).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build(),
                ActivityTransition.Builder().setActivityType(DetectedActivity.ON_FOOT).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build(),
                ActivityTransition.Builder().setActivityType(DetectedActivity.ON_BICYCLE).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build(),
                ActivityTransition.Builder().setActivityType(DetectedActivity.ON_BICYCLE).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build(),
                ActivityTransition.Builder().setActivityType(DetectedActivity.RUNNING).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build(),
                ActivityTransition.Builder().setActivityType(DetectedActivity.RUNNING).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build(),
                ActivityTransition.Builder().setActivityType(DetectedActivity.TILTING).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build(),
                ActivityTransition.Builder().setActivityType(DetectedActivity.TILTING).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build(),
                ActivityTransition.Builder().setActivityType(DetectedActivity.UNKNOWN).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER).build(),
                ActivityTransition.Builder().setActivityType(DetectedActivity.UNKNOWN).setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT).build(),
            )
        )
        val client = ActivityRecognition.getClient(applicationContext)
        val intent = Intent(applicationContext, ActivityTransitionReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        client.requestActivityTransitionUpdates(request, pendingIntent)
    }

    private fun sampleContextData() {
        getNewCalendarEvents() // calendar data
        getNewCalls() // call logs
    }

    private fun getNewCalendarEvents() {
        contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            arrayOf(
                CalendarContract.Events.ORIGINAL_ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
                CalendarContract.Events.EVENT_LOCATION,
            ),
            CalendarContract.Events.DELETED + " != 1",
            null,
            null,
        )?.let {
            val dao = DatabaseHelper.db.calendarEventDao()

            val originalIdIdx = it.getColumnIndex(CalendarContract.Events.ORIGINAL_ID)
            val titleIdx = it.getColumnIndex(CalendarContract.Events.TITLE)
            val startDateIdx = it.getColumnIndex(CalendarContract.Events.DTSTART)
            val endDateIdx = it.getColumnIndex(CalendarContract.Events.DTEND)
            val eventLocationIdx = it.getColumnIndex(CalendarContract.Events.EVENT_LOCATION)

            if (it.moveToFirst()) do {
                val startTs = it.getLong(startDateIdx)
                val eventId = "${it.getString(originalIdIdx)}_${startTs}"
                if (!dao.exists(eventId = eventId)) dao.insertAll(
                    CalendarEvent(
                        event_id = eventId,
                        title = it.getString(titleIdx),
                        start_ts = it.getLong(startDateIdx),
                        end_ts = it.getLong(endDateIdx),
                        event_location = it.getString(eventLocationIdx),
                        submitted = false,
                    )
                )
            } while (it.moveToNext())

            it.close()
        }
    }

    private fun getNewCalls() {
        baseContext.contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null)?.let {
            if (it.moveToFirst()) do {
                val callDate = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.DATE))
                val timestamp = Date(java.lang.Long.valueOf(callDate)).time
                val number = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                val duration = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.DURATION))
                val callType = when (it.getString(it.getColumnIndexOrThrow(CallLog.Calls.TYPE)).toInt()) {
                    CallLog.Calls.OUTGOING_TYPE -> "OUTGOING"
                    CallLog.Calls.INCOMING_TYPE -> "INCOMING"
                    CallLog.Calls.MISSED_TYPE -> "MISSED"
                    else -> "N/A"
                }
                DatabaseHelper.saveCallLog(
                    io.github.qobiljon.stressapp.core.database.data.CallLog(
                        timestamp = timestamp,
                        number = number,
                        duration = duration,
                        call_type = callType,
                        submitted = false,
                    )
                )
            } while (it.moveToNext())

            it.close()
        }
    }
}
