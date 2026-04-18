package com.tripsphere.domain.usecase

import com.tripsphere.domain.model.Itinerary
import com.tripsphere.domain.repository.ItineraryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItineraryUseCase @Inject constructor(
    private val repository: ItineraryRepository
) {
    operator fun invoke(tripId: Long): Flow<List<Itinerary>> = repository.getItineraryForTrip(tripId)
}

class AddItineraryUseCase @Inject constructor(
    private val repository: ItineraryRepository
) {
    suspend operator fun invoke(itinerary: Itinerary) = repository.insertItinerary(itinerary)
}

class DeleteItineraryUseCase @Inject constructor(
    private val repository: ItineraryRepository
) {
    suspend operator fun invoke(itinerary: Itinerary) = repository.deleteItinerary(itinerary)
}
