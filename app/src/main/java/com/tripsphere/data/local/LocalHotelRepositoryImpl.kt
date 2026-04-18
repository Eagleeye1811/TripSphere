package com.tripsphere.data.local

import com.tripsphere.domain.model.Hotel
import com.tripsphere.domain.repository.HotelRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.*

@Singleton
class LocalHotelRepositoryImpl @Inject constructor() : HotelRepository {

    override suspend fun getNearbyHotels(
        latitude: Double,
        longitude: Double,
        radiusMeters: Int
    ): Result<List<Hotel>> = runCatching {
        val radius = radiusMeters.toDouble()

        val nearby = HotelDataset.all.mapNotNull { entry ->
            val dist = haversineMeters(latitude, longitude, entry.latitude, entry.longitude)
            if (dist <= radius) entry to dist.toInt() else null
        }

        // If nothing found within the given radius, expand to 200 km to always show something
        val results = if (nearby.isEmpty()) {
            HotelDataset.all.map { entry ->
                val dist = haversineMeters(latitude, longitude, entry.latitude, entry.longitude)
                entry to dist.toInt()
            }.sortedBy { it.second }.take(10)
        } else {
            nearby.sortedBy { it.second }
        }

        results.map { (entry, dist) ->
            Hotel(
                id                  = entry.id,
                name                = entry.name,
                type                = entry.type,
                stars               = entry.stars,
                address             = entry.address,
                latitude            = entry.latitude,
                longitude           = entry.longitude,
                distanceMeters      = dist,
                phone               = entry.phone,
                website             = entry.website,
                estimatedPriceRange = entry.estimatedPriceRange,
                imageUrl            = entry.imageUrl
            )
        }
    }

    private fun haversineMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r    = 6_371_000.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a    = sin(dLat / 2).pow(2) +
                   cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
        return r * 2 * atan2(sqrt(a), sqrt(1 - a))
    }
}
