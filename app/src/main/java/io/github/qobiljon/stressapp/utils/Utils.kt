package io.github.qobiljon.stressapp.utils

import android.content.Context
import android.net.Uri
import android.widget.Toast

object Utils {
    fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getCalendarEvents(context: Context) {
        context.contentResolver.query(
            Uri.parse("content://com.android.calendar/events"),
            arrayOf("calendar_id", "title", "description", "dtstart", "dtend", "eventLocation"),
            null, null, null
        )?.let { cursor ->
            val names = mutableListOf<String>()
            val descriptions = mutableListOf<String>()
            val starts = mutableListOf<String>()
            val ends = mutableListOf<String>()

            if (cursor.moveToFirst())
                do {
                    names.add(cursor.getString(cursor.getColumnIndex("")))
                } while (cursor.moveToNext())

            cursor.close()
        }
    }
}