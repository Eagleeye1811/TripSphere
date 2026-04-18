package com.tripsphere.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tripsphere.data.local.dao.ExpenseDao
import com.tripsphere.data.local.dao.ItineraryDao
import com.tripsphere.data.local.dao.TripDao
import com.tripsphere.data.local.dao.UserDao
import com.tripsphere.data.local.entity.ExpenseEntity
import com.tripsphere.data.local.entity.ItineraryEntity
import com.tripsphere.data.local.entity.TripEntity
import com.tripsphere.data.local.entity.UserEntity

@Database(
    entities = [TripEntity::class, ExpenseEntity::class, ItineraryEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TripSphereDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun itineraryDao(): ItineraryDao
    abstract fun userDao(): UserDao
}
