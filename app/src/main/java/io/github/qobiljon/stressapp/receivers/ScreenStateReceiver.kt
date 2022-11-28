package io.github.qobiljon.stressapp.receivers

import android.app.KeyguardManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import io.github.qobiljon.stressapp.core.database.DatabaseHelper
import io.github.qobiljon.stressapp.core.database.data.ScreenState
import io.github.qobiljon.stressapp.ui.MainActivity

class ScreenStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val timestamp = System.currentTimeMillis()
        val screenState = when (intent.action) {
            Intent.ACTION_USER_PRESENT -> "USER_PRESENT"
            Intent.ACTION_SCREEN_OFF -> "SCREEN_OFF"
            Intent.ACTION_SCREEN_ON -> "SCREEN_ON"
            else -> "N/A"
        }
        val myKM = context.getSystemService(Service.KEYGUARD_SERVICE) as KeyguardManager
        val restrictedInputMode = myKM.inKeyguardRestrictedInputMode()
        Log.e(MainActivity.TAG, "$timestamp, $screenState, $restrictedInputMode")

        DatabaseHelper.saveScreenState(
            ScreenState(
                timestamp = timestamp,
                screen_state = screenState,
                keyguard_restricted_input_mode = restrictedInputMode,
            )
        )
    }
}
