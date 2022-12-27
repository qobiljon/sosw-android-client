package io.github.qobiljon.stressapp

import android.app.Application
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import io.github.qobiljon.stressapp.core.database.DatabaseHelper


@HiltAndroidApp
class AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        DatabaseHelper.init(applicationContext)
        FirebaseApp.initializeApp(applicationContext)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) return@OnCompleteListener
            val token = task.result
            DatabaseHelper.setFcmToken(applicationContext, fcmToken = token)
        })
    }
}