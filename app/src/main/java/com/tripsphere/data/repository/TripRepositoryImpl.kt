package com.tripsphere.data.repository

import com.tripsphere.data.local.dao.TripDao
import com.tripsphere.data.local.entity.TripEntity
import com.tripsphere.domain.model.Trip
import com.tripsphere.domain.model.TripStatus
import com.tripsphere.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val dao: TripDao
) : TripRepository {

    override fun getAllTrips(): Flow<List<Trip>> =
        dao.getAllTrips().map { it.map(::entityToDomain) }

    override suspend fun getTripById(id: Long): Trip? =
        dao.getTripById(id)?.let(::entityToDomain)

    override suspend fun insertTrip(trip: Trip): Long =
        dao.insertTrip(domainToEntity(trip))

    override suspend fun updateTrip(trip: Trip) =
        dao.updateTrip(domainToEntity(trip))

    override suspend fun deleteTrip(trip: Trip) =
        dao.deleteTrip(domainToEntity(trip))

    override fun getActiveTrips(): Flow<List<Trip>> =
        dao.getActiveTrips().map { it.map(::entityToDomain) }

    private fun entityToDomain(entity: TripEntity) = Trip(
        id = entity.id,
        destination = entity.destination,
        startDate = LocalDate.parse(entity.startDate),
        endDate = LocalDate.parse(entity.endDate),
        budget = entity.budget,
        imageUrl = entity.imageUrl,
        status = TripStatus.valueOf(entity.status),
        notes = entity.notes
    )

    private fun domainToEntity(trip: Trip) = TripEntity(
        id = trip.id,
        destination = trip.destination,
        startDate = trip.startDate.toString(),
        endDate = trip.endDate.toString(),
        budget = trip.budget,
        imageUrl = trip.imageUrl,
        status = trip.status.name,
        notes = trip.notes
    )
}
