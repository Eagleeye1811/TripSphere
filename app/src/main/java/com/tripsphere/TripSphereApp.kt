package com.tripsphere

import android.app.Application
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.tripsphere.utils.NotificationHelper
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class TripSphereApp : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    /**
     * Provide a custom Coil ImageLoader that sets the User-Agent header required
     * by Wikipedia's image CDN. Without this, Wikimedia thumbnails are throttled/rejected.
     */
    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this)
        .okHttpClient {
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    chain.proceed(
                        chain.request().newBuilder()
                            .header("User-Agent", "TripSphere/1.0 (Android; https://tripsphere.app)")
                            .build()
                    )
                }
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        }
        .crossfade(true)
        .build()

    private fun createNotificationChannels() {
        val tripChannel = NotificationChannelCompat.Builder(
            NotificationHelper.TRIP_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_HIGH
        )
            .setName("Trip Reminders")
            .setDescription("Reminders for your upcoming trips and daily itinerary")
            .setVibrationEnabled(true)
            .build()

        val budgetChannel = NotificationChannelCompat.Builder(
            NotificationHelper.BUDGET_CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_HIGH
        )
            .setName("Budget Alerts")
            .setDescription("Alerts when your trip budget is running low or exceeded")
            .setVibrationEnabled(true)
            .build()

        NotificationManagerCompat.from(this).apply {
            createNotificationChannel(tripChannel)
            createNotificationChannel(budgetChannel)
        }
    }
}
