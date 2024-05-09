package com.belajar.flodec.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.belajar.flodec.MainActivity
import com.belajar.flodec.R
//import androidx.work.OneTimeWorkRequest
//import androidx.work.WorkManager
//import androidx.work.Worker
//import androidx.work.WorkerParameters
//import com.google.firebase.example.messaging.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

public class MyFirebaseMessagingService: FirebaseMessagingService() {
    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

    private var Status: String? = null
    private var Ketinggian: String? = null
    private var Intensitas: String? = null
    private var Debit: String? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
//        if (remoteMessage.data.isNotEmpty()) {
//            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
//            Status     = remoteMessage.data.get("Status")
//            Ketinggian = remoteMessage.data.get("Ketinggian")
//            Intensitas = remoteMessage.data.get("Intensitas")
//            Debit = remoteMessage.data.get("Debit")
//        }

        if(remoteMessage.notification != null){
            sendNotification(remoteMessage.notification?.body)
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // Kirim Token yang diperbarui ke server
    }

    @RequiresApi(Build.VERSION_CODES.S) // seharusnya gk ada, cuma karena mau buat notif yg gede harus ada
    private fun sendNotification(messageBody: String?) {
        val MapIntent = Intent(this, MainActivity::class.java)
//        MapIntent.putExtra("Status",Status)
//        MapIntent.putExtra("Ketinggian", Ketinggian)
//        MapIntent.putExtra("Intensitas",Intensitas)
//        MapIntent.putExtra("Debit",Debit)
        MapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val requestCode = 1
//        val pendingIntent = TaskStackBuilder.create(this).run {
//            addNextIntentWithParentStack(MapIntent)
//            getPendingIntent(requestCode, PendingIntent.FLAG_IMMUTABLE)
//        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            MapIntent,
            PendingIntent.FLAG_IMMUTABLE,
        )

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_info_24)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(messageBody)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(messageBody))
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = 1
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}