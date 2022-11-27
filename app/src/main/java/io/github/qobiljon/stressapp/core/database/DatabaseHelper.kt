package io.github.qobiljon.stressapp.core.database

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.room.Room
import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.core.api.ApiHelper
import io.github.qobiljon.stressapp.core.database.data.SelfReport
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
                val selfReportDao = db.selfReportDao()
                for (selfReport in selfReportDao.getAll()) {
                    val success = ApiHelper.submitSelfReport(context, token = getAuthToken(context), selfReport = selfReport)
                    if (success) selfReportDao.delete(selfReport)
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
}
