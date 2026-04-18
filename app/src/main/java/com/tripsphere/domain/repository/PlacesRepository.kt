package com.tripsphere.domain.repository

import com.tripsphere.domain.model.Destination
import com.tripsphere.domain.model.DestinationCategory
import com.tripsphere.domain.model.Place
import com.tripsphere.domain.model.PlaceSuggestion

interface PlacesRepository {
    suspend fun getNearbyPlaces(latitude: Double, longitude: Double, radiusMeters: Int = 3000): Result<List<Place>>
    suspend fun autocomplete(query: String, latitude: Double? = null, longitude: Double? = null): Result<List<PlaceSuggestion>>
    suspend fun getPlacePhotos(placeId: String): Result<List<String>>
    suspend fun fetchTouristPlaces(category: DestinationCategory): Result<List<Destination>>
}
