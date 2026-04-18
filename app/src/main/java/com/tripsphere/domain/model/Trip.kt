package com.tripsphere.domain.model

import java.time.LocalDate

data class Trip(
    val id: Long = 0,
    val destination: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val budget: Double,
    val imageUrl: String = "",
    val status: TripStatus = TripStatus.UPCOMING,
    val notes: String = ""
)

enum class TripStatus {
    UPCOMING, ONGOING, COMPLETED
}
