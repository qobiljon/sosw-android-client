package io.github.qobiljon.stressapp.utils

import android.content.Context
import android.widget.Toast
import java.util.*

object Utils {
    fun validDate(dateStr: String): Boolean {
        if (dateStr.length != 8) return false

        try {
            val year = Integer.parseInt(dateStr.substring(0, 4))
            val month = Integer.parseInt(dateStr.substring(4, 6))
            val day = Integer.parseInt(dateStr.substring(6, 8))
            Date(year, month, day)
        } catch (e: Exception) {
            return false
        }

        return true
    }

    fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}