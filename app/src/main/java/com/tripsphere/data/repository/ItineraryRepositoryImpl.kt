package com.tripsphere.data.repository

import com.tripsphere.data.local.dao.ItineraryDao
import com.tripsphere.data.local.entity.ItineraryEntity
import com.tripsphere.domain.model.Itinerary
import com.tripsphere.domain.repository.ItineraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ItineraryRepositoryImpl @Inject constructor(
    private val dao: ItineraryDao
) : ItineraryRepository {

    override fun getItineraryForTrip(tripId: Long): Flow<List<Itinerary>> =
        dao.getItineraryForTrip(tripId).map { it.map(::entityToDomain) }

    override suspend fun insertItinerary(itinerary: Itinerary) =
        dao.insertItinerary(domainToEntity(itinerary))

    override suspend fun updateItinerary(itinerary: Itinerary) =
        dao.updateItinerary(domainToEntity(itinerary))

    override suspend fun deleteItinerary(itinerary: Itinerary) =
        dao.deleteItinerary(domainToEntity(itinerary))

    private fun entityToDomain(entity: ItineraryEntity) = Itinerary(
        id = entity.id,
        tripId = entity.tripId,
        day = entity.day,
        time = entity.time,
        activity = entity.activity,
        notes = entity.notes
    )

    private fun domainToEntity(itinerary: Itinerary) = ItineraryEntity(
        id = itinerary.id,
        tripId = itinerary.tripId,
        day = itinerary.day,
        time = itinerary.time,
        activity = itinerary.activity,
        notes = itinerary.notes
    )
}
