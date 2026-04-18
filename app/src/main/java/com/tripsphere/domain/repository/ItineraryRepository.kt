package com.tripsphere.domain.repository

import com.tripsphere.domain.model.Itinerary
import kotlinx.coroutines.flow.Flow

interface ItineraryRepository {
    fun getItineraryForTrip(tripId: Long): Flow<List<Itinerary>>
    suspend fun insertItinerary(itinerary: Itinerary)
    suspend fun updateItinerary(itinerary: Itinerary)
    suspend fun deleteItinerary(itinerary: Itinerary)
}
