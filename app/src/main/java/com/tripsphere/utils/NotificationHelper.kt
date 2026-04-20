package com.tripsphere.utils

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tripsphere.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val TRIP_CHANNEL_ID = "trip_reminders"
        const val BUDGET_CHANNEL_ID = "budget_alerts"

        private const val NOTIF_ID_TRIP_REMINDER = 1001
        private const val NOTIF_ID_BUDGET_ALERT = 1002
        private const val NOTIF_ID_ITINERARY = 1003
    }

    private fun hasPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    private fun buildOpenAppIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun sendTripDepartureReminder(tripName: String, daysUntil: Int) {
        if (!hasPermission()) return
        val body = when (daysUntil) {
            0 -> "$tripName starts today! Have an amazing trip! ✈️"
            1 -> "$tripName starts tomorrow — time to pack! 🧳"
            else -> "$tripName starts in $daysUntil days. Get ready!"
        }
        val notification = NotificationCompat.Builder(context, TRIP_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Trip Start ✈️")
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(buildOpenAppIntent())
            .build()
        NotificationManagerCompat.from(context).notify(NOTIF_ID_TRIP_REMINDER, notification)
    }

    fun sendBudgetAlert(tripName: String, spent: Double, budget: Double) {
        if (!hasPermission()) return
        val pct = ((spent / budget) * 100).toInt().coerceAtMost(100)
        val isExceeded = spent > budget
        val title = if (isExceeded) "Budget Exceeded! 🚨" else "Budget Warning ⚠️"
        val body = if (isExceeded)
            "$tripName: You've exceeded your \$${"%.0f".format(budget)} budget by \$${"%.0f".format(spent - budget)}!"
        else
            "$tripName: $pct% of your \$${"%.0f".format(budget)} budget used (\$${"%.0f".format(spent)} spent)"

        val notification = NotificationCompat.Builder(context, BUDGET_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(buildOpenAppIntent())
            .build()
        NotificationManagerCompat.from(context).notify(NOTIF_ID_BUDGET_ALERT, notification)
    }

    fun sendItineraryReminder(tripName: String, activityName: String, time: String) {
        if (!hasPermission()) return
        val body = "Today in $tripName: $activityName at $time 🗓️"
        val notification = NotificationCompat.Builder(context, TRIP_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_today)
            .setContentTitle("Today's Activity")
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(buildOpenAppIntent())
            .build()
        NotificationManagerCompat.from(context).notify(NOTIF_ID_ITINERARY, notification)
    }
}
