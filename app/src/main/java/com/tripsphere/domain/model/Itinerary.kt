package com.tripsphere.domain.model

data class Itinerary(
    val id: Long = 0,
    val tripId: Long,
    val day: Int,
    val time: String,
    val activity: String,
    val notes: String = ""
)
