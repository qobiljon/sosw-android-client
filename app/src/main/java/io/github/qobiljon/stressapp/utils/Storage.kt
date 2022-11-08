package io.github.qobiljon.stressapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.room.Room
import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.core.data.AppDatabase
import io.github.qobiljon.stressapp.core.data.SelfReport
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object Storage {
    private const val KEY_PREFS_NAME = "shared_prefs"
    private const val KEY_FULL_NAME = "full_name"
    private const val KEY_DATE_OF_BIRTH = "date_of_birth"
    private const val KEY_FCM_TOKEN = "fcm_token"
    private const val BATCH_SUBMIT_AMOUNT = 100

    private lateinit var db: AppDatabase

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(KEY_PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun init(context: Context) {
        db = Room.databaseBuilder(context, AppDatabase::class.java, context.getString(R.string.room_db_name)).allowMainThreadQueries().build()
    }

    fun syncToCloud(context: Context) {
        if (!isAuthenticated(context)) return

        runBlocking {
            val selfReportDao = db.selfReportDao()
            launch {
                var chunk: List<SelfReport>
                do {
                    chunk = selfReportDao.getK(k = BATCH_SUBMIT_AMOUNT)
                    if (chunk.isEmpty()) break

                    val success = Api.submitSelfReport(
                        context,
                        fullName = getFullName(context),
                        dateOfBirth = getDateOfBirth(context),
                        selfReports = chunk,
                    )
                    if (success) chunk.forEach { selfReportDao.delete(it) }
                    else break
                } while (chunk.size == BATCH_SUBMIT_AMOUNT)
            }
        }
    }

    fun isAuthenticated(context: Context): Boolean {
        return getSharedPreferences(context).getString(KEY_FULL_NAME, null) != null && getSharedPreferences(context).getString(KEY_DATE_OF_BIRTH, null) != null
    }

    fun getFullName(context: Context): String {
        return getSharedPreferences(context).getString(KEY_FULL_NAME, null)!!
    }

    fun getDateOfBirth(context: Context): String {
        return getSharedPreferences(context).getString(KEY_DATE_OF_BIRTH, null)!!
    }

    fun getFCMToken(context: Context): String {
        return getSharedPreferences(context).getString(KEY_FCM_TOKEN, null)!!
    }

    fun setFullName(context: Context, fullName: String) {
        getSharedPreferences(context).edit {
            putString(KEY_FULL_NAME, fullName)
        }
    }

    fun setDateOfBirth(context: Context, dateOfBirth: String) {
        getSharedPreferences(context).edit {
            putString(KEY_DATE_OF_BIRTH, dateOfBirth)
        }
    }

    fun setFCMToken(context: Context, fcmToken: String) {
        getSharedPreferences(context).edit {
            putString(KEY_FCM_TOKEN, fcmToken)
        }
    }

    fun saveSelfReport(selfReport: SelfReport) {
        db.selfReportDao().insertAll(selfReport)
    }
}