package io.github.qobiljon.stressapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object Storage {
    private const val KEY_PREFS_NAME = "shared_prefs"
    private const val KEY_FULL_NAME = "full_name"
    private const val KEY_DATE_OF_BIRTH = "date_of_birth"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(KEY_PREFS_NAME, Context.MODE_PRIVATE)
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

    fun saveEMA(
        context: Context,
        timestamp: Long,
        pss_control: Int,
        pss_confident: Int,
        pss_yourway: Int,
        pss_difficulties: Int,
        stresslvl: Int,
        social_settings: String,
        location: String,
        activity: String,
    ) {
        // todo do dis
    }
}
