package com.tripsphere.domain.repository

import com.tripsphere.domain.model.Hotel

interface HotelRepository {
    suspend fun getNearbyHotels(
        latitude: Double,
        longitude: Double,
        radiusMeters: Int = 5000
    ): Result<List<Hotel>>
}
