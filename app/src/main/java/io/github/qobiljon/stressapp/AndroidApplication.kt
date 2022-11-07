package io.github.qobiljon.stressapp

import android.app.Application
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import io.github.qobiljon.stressapp.utils.Storage


class AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Storage.init(applicationContext)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) return@OnCompleteListener
            val token = task.result
            Storage.setFCMToken(applicationContext, fcmToken = token)
        })
    }
}