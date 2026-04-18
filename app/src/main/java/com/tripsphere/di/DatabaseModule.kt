package com.tripsphere.di

import android.content.Context
import androidx.room.Room
import com.tripsphere.data.local.TripSphereDatabase
import com.tripsphere.data.local.dao.ExpenseDao
import com.tripsphere.data.local.dao.ItineraryDao
import com.tripsphere.data.local.dao.TripDao
import com.tripsphere.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTripSphereDatabase(@ApplicationContext context: Context): TripSphereDatabase {
        return Room.databaseBuilder(
            context,
            TripSphereDatabase::class.java,
            "tripsphere_db"
        ).build()
    }

    @Provides
    fun provideTripDao(db: TripSphereDatabase): TripDao = db.tripDao()

    @Provides
    fun provideExpenseDao(db: TripSphereDatabase): ExpenseDao = db.expenseDao()

    @Provides
    fun provideItineraryDao(db: TripSphereDatabase): ItineraryDao = db.itineraryDao()

    @Provides
    fun provideUserDao(db: TripSphereDatabase): UserDao = db.userDao()
}
