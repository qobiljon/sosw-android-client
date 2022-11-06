package io.github.qobiljon.stressapp.extensions

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.github.qobiljon.stressapp.R
import io.github.qobiljon.stressapp.ui.MainActivity
import io.github.qobiljon.stressapp.utils.Api
import io.github.qobiljon.stressapp.utils.Storage
import kotlinx.coroutines.runBlocking

class FCMInstanceIDService : FirebaseMessagingService() {
    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "sosw.app.push"
        private const val NOTIFICATION_CHANNEL_NAME = "sosw_app_push"
    }

    override fun onNewToken(token: String) {
        Storage.setFCMToken(applicationContext, fcmToken = token)
        if (Storage.isAuthenticated(applicationContext)) runBlocking {
            Api.authenticate(
                applicationContext,
                fullName = Storage.getFullName(applicationContext),
                dateOfBirth = Storage.getDateOfBirth(applicationContext),
                fcmToken = token,
            )
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val builder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stress_app)
            .setAutoCancel(false)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(false)
            .setContentIntent(PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE))
            .setContent(RemoteViews("io.github.qobiljon.stressapp", R.layout.push_notification))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)

        notificationManager.notify(0, builder.build())
    }
}