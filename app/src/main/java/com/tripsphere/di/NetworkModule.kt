package com.tripsphere.di

import com.tripsphere.data.local.LocalHotelRepositoryImpl
import com.tripsphere.data.remote.api.NominatimApiService
import com.tripsphere.data.remote.api.WeatherApiService
import com.tripsphere.data.remote.api.WikimediaApiService
import com.tripsphere.data.remote.repository.PlacesRepositoryImpl
import com.tripsphere.data.remote.repository.WeatherRepositoryImpl
import com.tripsphere.domain.repository.HotelRepository
import com.tripsphere.domain.repository.PlacesRepository
import com.tripsphere.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Wikipedia and Nominatim both require a descriptive User-Agent per their usage policies.
     * Without it, requests are throttled or rejected — causing the "offline data" fallback.
     */
    private val userAgentInterceptor = Interceptor { chain ->
        chain.proceed(
            chain.request().newBuilder()
                .header("User-Agent", "TripSphere/1.0 (Android; https://tripsphere.app)")
                .build()
        )
    }

    @Provides @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(userAgentInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    // ── Open-Meteo (weather — free, no key) ──────────────────────────────────

    @Provides @Singleton @Named("weather")
    fun provideWeatherRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl("https://api.open-meteo.com/")
            .client(client).addConverterFactory(GsonConverterFactory.create()).build()

    @Provides @Singleton
    fun provideWeatherApiService(@Named("weather") r: Retrofit): WeatherApiService =
        r.create(WeatherApiService::class.java)

    @Provides @Singleton
    fun provideWeatherRepository(api: WeatherApiService): WeatherRepository =
        WeatherRepositoryImpl(api)

    // ── Wikipedia / Wikimedia (tourist places, photos — free, no key) ─────────

    @Provides @Singleton @Named("wikipedia")
    fun provideWikipediaRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl("https://en.wikipedia.org/")
            .client(client).addConverterFactory(GsonConverterFactory.create()).build()

    @Provides @Singleton
    fun provideWikimediaApiService(@Named("wikipedia") r: Retrofit): WikimediaApiService =
        r.create(WikimediaApiService::class.java)

    // ── Nominatim (autocomplete — free, no key) ───────────────────────────────

    @Provides @Singleton @Named("nominatim")
    fun provideNominatimRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl("https://nominatim.openstreetmap.org/")
            .client(client).addConverterFactory(GsonConverterFactory.create()).build()

    @Provides @Singleton
    fun provideNominatimApiService(@Named("nominatim") r: Retrofit): NominatimApiService =
        r.create(NominatimApiService::class.java)

    // ── Places Repository ─────────────────────────────────────────────────────

    @Provides @Singleton
    fun providePlacesRepository(
        wiki: WikimediaApiService,
        nominatim: NominatimApiService
    ): PlacesRepository = PlacesRepositoryImpl(wiki, nominatim)

    // ── Hotels — served from local dataset (no network call needed) ───────────
    @Provides @Singleton
    fun provideHotelRepository(): HotelRepository = LocalHotelRepositoryImpl()
}
