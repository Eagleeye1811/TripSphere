package com.tripsphere.domain.usecase

import com.tripsphere.domain.model.Trip
import com.tripsphere.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTripsUseCase @Inject constructor(
    private val repository: TripRepository
) {
    operator fun invoke(): Flow<List<Trip>> = repository.getAllTrips()
}

class GetTripByIdUseCase @Inject constructor(
    private val repository: TripRepository
) {
    suspend operator fun invoke(id: Long): Trip? = repository.getTripById(id)
}

class SaveTripUseCase @Inject constructor(
    private val repository: TripRepository
) {
    suspend operator fun invoke(trip: Trip): Long = repository.insertTrip(trip)
}

class UpdateTripUseCase @Inject constructor(
    private val repository: TripRepository
) {
    suspend operator fun invoke(trip: Trip) = repository.updateTrip(trip)
}

class DeleteTripUseCase @Inject constructor(
    private val repository: TripRepository
) {
    suspend operator fun invoke(trip: Trip) = repository.deleteTrip(trip)
}

class GetActiveTripsUseCase @Inject constructor(
    private val repository: TripRepository
) {
    operator fun invoke(): Flow<List<Trip>> = repository.getActiveTrips()
}
