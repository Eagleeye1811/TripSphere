package com.tripsphere.di

import com.tripsphere.data.repository.ExpenseRepositoryImpl
import com.tripsphere.data.repository.ItineraryRepositoryImpl
import com.tripsphere.data.repository.TripRepositoryImpl
import com.tripsphere.data.repository.UserRepositoryImpl
import com.tripsphere.domain.repository.ExpenseRepository
import com.tripsphere.domain.repository.ItineraryRepository
import com.tripsphere.domain.repository.TripRepository
import com.tripsphere.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTripRepository(impl: TripRepositoryImpl): TripRepository

    @Binds
    @Singleton
    abstract fun bindExpenseRepository(impl: ExpenseRepositoryImpl): ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindItineraryRepository(impl: ItineraryRepositoryImpl): ItineraryRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}
