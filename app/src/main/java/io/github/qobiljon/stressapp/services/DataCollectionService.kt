package io.github.qobiljon.stressapp.services

import android.app.*
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.IBinder
import android.provider.CalendarContract
import android.util.Log
import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.core.database.DatabaseHelper
import io.github.qobiljon.stressapp.core.database.data.CalendarEvent
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

    private fun testCalendarData() {
        contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            arrayOf(
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            ),
            null,
            null,
            null,
        )?.let { cursor1 ->
            if (cursor1.moveToFirst()) do {
                val calendarId = cursor1.getString(0)
                val displayName = cursor1.getString(1)

                val stringLength = displayName.length
                val output = displayName.substring(stringLength - 10)
                Log.d("Cursor", output)

                val builder = CalendarContract.Instances.CONTENT_URI.buildUpon()

                val beginTime = Calendar.getInstance()
                beginTime[Calendar.YEAR] = 2021
                beginTime[Calendar.MONTH] = Calendar.NOVEMBER
                beginTime[Calendar.DAY_OF_MONTH] = 30
                val startMills = beginTime.timeInMillis

                val endTime = Calendar.getInstance()
                endTime[Calendar.YEAR] = 2022
                endTime[Calendar.MONTH] = Calendar.NOVEMBER
                endTime[Calendar.DAY_OF_MONTH] = 30
                val endMills = endTime.timeInMillis

                ContentUris.appendId(builder, startMills)
                ContentUris.appendId(builder, endMills)

                contentResolver.query(
                    builder.build(),
                    arrayOf(
                        CalendarContract.Instances.TITLE,
                        CalendarContract.Instances.BEGIN,
                        CalendarContract.Instances.END,
                        CalendarContract.Instances.DESCRIPTION,
                    ),
                    CalendarContract.Instances.CALENDAR_ID + " = ?",
                    arrayOf(calendarId),
                    null,
                )?.let { cursor2 ->
                    if (cursor2.moveToFirst()) do {
                        val title = cursor2.getString(0);
                        val begin = cursor2.getLong(1)
                        val end = cursor2.getLong(2)
                        val description = cursor2.getString(3)

                        Log.d("Cursor", "Title: $title\tDescription: $description\tBegin: $begin\tEnd: $end");
                    } while (cursor2.moveToNext())
                    cursor2.close()
                }
            } while (cursor1.moveToNext())
            cursor1.close()
        }
    }

    private fun sampleContextData() {
        testCalendarData()

        // calendar data
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
                        is_submitted = false,
                    )
                )
            } while (it.moveToNext())

            it.close()
        }
    }
}
