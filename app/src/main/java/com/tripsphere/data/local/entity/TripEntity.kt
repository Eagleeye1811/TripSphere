package com.tripsphere.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val destination: String,
    val startDate: String,      // ISO format: yyyy-MM-dd
    val endDate: String,        // ISO format: yyyy-MM-dd
    val budget: Double,
    val imageUrl: String = "",
    val status: String = "UPCOMING",
    val notes: String = ""
)
