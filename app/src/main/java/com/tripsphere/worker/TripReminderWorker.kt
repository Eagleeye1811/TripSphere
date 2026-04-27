package com.tripsphere.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tripsphere.utils.NotificationHelper

class TripReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val tripName = inputData.getString(KEY_TRIP_NAME) ?: return Result.failure()
        val daysUntil = inputData.getInt(KEY_DAYS_UNTIL, 1)

        if (!hasNotificationPermission()) return Result.success()

        val body = when (daysUntil) {
            0 -> "$tripName starts today! Have an amazing trip! ✈️"
            1 -> "$tripName starts tomorrow — time to pack! 🧳"
            else -> "$tripName starts in $daysUntil days. Get ready! 🗺️"
        }

        val notification = NotificationCompat.Builder(applicationContext, NotificationHelper.TRIP_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Trip Start ✈️")
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext)
            .notify(NOTIFICATION_ID + daysUntil, notification)

        return Result.success()
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                applicationContext, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    companion object {
        const val KEY_TRIP_NAME = "trip_name"
        const val KEY_DAYS_UNTIL = "days_until"
        const val NOTIFICATION_ID = 2000
    }
}
