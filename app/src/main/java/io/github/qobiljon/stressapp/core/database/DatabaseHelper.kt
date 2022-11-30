package io.github.qobiljon.stressapp.core.database

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.room.Room
import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.core.api.ApiHelper
import io.github.qobiljon.stressapp.core.database.data.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object DatabaseHelper {
    private const val KEY_PREFS_NAME = "shared_prefs"
    private const val KEY_AUTH_TOKEN = "auth_token"
    private const val KEY_FCM_TOKEN = "fcm_token"
    lateinit var db: AppDatabase

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(KEY_PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun init(context: Context) {
        db = Room.databaseBuilder(context, AppDatabase::class.java, context.getString(R.string.room_db_name)).allowMainThreadQueries().build()
    }

    fun syncToCloud(context: Context) {
        if (!isAuthenticated(context)) return

        runBlocking {
            launch { ApiHelper.setFcmToken(context, token = getAuthToken(context), fcmToken = getFcmToken(context)) }
            launch {
                val dao = db.selfReportDao()
                for (selfReport in dao.getAll()) {
                    val success = ApiHelper.submitSelfReport(context, token = getAuthToken(context), selfReport = selfReport)
                    if (success) dao.delete(selfReport)
                }
            }
            launch {
                val dao = db.locationDao()
                for (location in dao.getAll()) {
                    val success = ApiHelper.submitLocation(context, token = getAuthToken(context), location = location)
                    if (success) dao.delete(location)
                }
            }
            launch {
                val dao = db.screenStateDao()
                for (screenState in dao.getAll()) {
                    val success = ApiHelper.submitScreenState(context, token = getAuthToken(context), screenState = screenState)
                    if (success) dao.delete(screenState)
                }
            }
            launch {
                val dao = db.callLogDao()
                for (callLog in dao.getFiltered(submitted = false)) {
                    val success = ApiHelper.submitCallLog(context, token = getAuthToken(context), callLog = callLog)
                    if (success) dao.setSubmitted(submitted = true, timestamp = callLog.timestamp)
                }
            }
            launch {
                val dao = db.calendarEventDao()
                for (calendarEvent in dao.getFiltered(submitted = false)) {
                    val success = ApiHelper.submitCalendarEvent(context, token = getAuthToken(context), calendarEvent = calendarEvent)
                    if (success) dao.setSubmitted(submitted = true, eventId = calendarEvent.event_id)
                }
            }
            launch {
                val dao = db.activityTransitionDao()
                for (activityTransition in dao.getAll()) {
                    val success = ApiHelper.submitActivityTransition(context, token = getAuthToken(context), activityTransition = activityTransition)
                    if (success) dao.delete(activityTransition)
                }
            }
            launch {
                val dao = db.activityRecognitionDao()
                for (activityRecognition in dao.getAll()) {
                    val success = ApiHelper.submitActivityRecognition(context, token = getAuthToken(context), activityRecognition = activityRecognition)
                    if (success) dao.delete(activityRecognition)
                }
            }
        }
    }

    fun isAuthenticated(context: Context): Boolean {
        return getSharedPreferences(context).getString(KEY_AUTH_TOKEN, null) != null
    }

    fun getAuthToken(context: Context): String {
        return getSharedPreferences(context).getString(KEY_AUTH_TOKEN, null)!!
    }

    fun setAuthToken(context: Context, authToken: String) {
        getSharedPreferences(context).edit { putString(KEY_AUTH_TOKEN, authToken) }
    }

    fun hasFcmToken(context: Context): Boolean {
        return getSharedPreferences(context).getString(KEY_FCM_TOKEN, null) != null
    }

    fun getFcmToken(context: Context): String {
        return getSharedPreferences(context).getString(KEY_FCM_TOKEN, null)!!
    }

    fun setFcmToken(context: Context, fcmToken: String) {
        getSharedPreferences(context).edit { putString(KEY_FCM_TOKEN, fcmToken) }
    }

    fun saveSelfReport(selfReport: SelfReport) {
        db.selfReportDao().insertAll(selfReport)
    }

    fun saveLocation(location: Location) {
        if (!db.locationDao().exists(location.timestamp)) db.locationDao().insertAll(location)
    }

    fun saveScreenState(screenState: ScreenState) {
        db.screenStateDao().insertAll(screenState)
    }

    fun saveActivityTransition(activityTransition: ActivityTransition) {
        db.activityTransitionDao().insertAll(activityTransition)
    }

    fun saveActivityRecognition(activityRecognition: ActivityRecognition) {
        db.activityRecognitionDao().insertAll(activityRecognition)
    }

    fun saveCallLog(callLog: CallLog) {
        if (!db.callLogDao().exists(callLog.timestamp)) db.callLogDao().insertAll(callLog)
    }
}
