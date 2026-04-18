package com.tripsphere.utils

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.tripsphere.worker.ItineraryReminderWorker
import com.tripsphere.worker.TripReminderWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val workManager get() = WorkManager.getInstance(context)

    fun scheduleTripReminders(tripId: Long, tripName: String, startDate: LocalDate) {
        val today = LocalDate.now()
        val daysUntilTrip = ChronoUnit.DAYS.between(today, startDate)

        // 1-day-before reminder
        if (daysUntilTrip >= 2) {
            val delayDays = daysUntilTrip - 1
            val request = OneTimeWorkRequestBuilder<TripReminderWorker>()
                .setInitialDelay(delayDays, TimeUnit.DAYS)
                .setInputData(
                    workDataOf(
                        TripReminderWorker.KEY_TRIP_NAME to tripName,
                        TripReminderWorker.KEY_DAYS_UNTIL to 1
                    )
                )
                .addTag(tagDayBefore(tripId))
                .build()
            workManager.enqueue(request)
        }

        // Day-of reminder (same day or future)
        if (daysUntilTrip >= 0) {
            val request = OneTimeWorkRequestBuilder<TripReminderWorker>()
                .setInitialDelay(daysUntilTrip, TimeUnit.DAYS)
                .setInputData(
                    workDataOf(
                        TripReminderWorker.KEY_TRIP_NAME to tripName,
                        TripReminderWorker.KEY_DAYS_UNTIL to 0
                    )
                )
                .addTag(tagDayOf(tripId))
                .build()
            workManager.enqueue(request)
        }

        // Daily itinerary reminder during trip (every 24h starting at trip start)
        val tripDurationDays = ChronoUnit.DAYS.between(startDate, startDate.plusDays(1))
            .coerceAtLeast(1)
        val initialDelayForDaily = if (daysUntilTrip >= 0) daysUntilTrip else 0L

        val dailyRequest = PeriodicWorkRequestBuilder<ItineraryReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelayForDaily, TimeUnit.DAYS)
            .setInputData(workDataOf(ItineraryReminderWorker.KEY_TRIP_NAME to tripName))
            .addTag(tagDaily(tripId))
            .build()
        workManager.enqueue(dailyRequest)
    }

    fun cancelTripReminders(tripId: Long) {
        workManager.cancelAllWorkByTag(tagDayBefore(tripId))
        workManager.cancelAllWorkByTag(tagDayOf(tripId))
        workManager.cancelAllWorkByTag(tagDaily(tripId))
    }

    private fun tagDayBefore(id: Long) = "trip_reminder_before_$id"
    private fun tagDayOf(id: Long) = "trip_reminder_dayof_$id"
    private fun tagDaily(id: Long) = "trip_reminder_daily_$id"
}
