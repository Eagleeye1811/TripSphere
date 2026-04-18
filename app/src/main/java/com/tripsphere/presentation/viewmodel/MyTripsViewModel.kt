package com.tripsphere.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tripsphere.domain.model.Trip
import com.tripsphere.domain.model.TripStatus
import com.tripsphere.domain.usecase.DeleteTripUseCase
import com.tripsphere.domain.usecase.GetAllTripsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class MyTripsUiState(
    val isLoading: Boolean = true,
    val upcomingTrips: List<Trip> = emptyList(),
    val ongoingTrips: List<Trip> = emptyList(),
    val completedTrips: List<Trip> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class MyTripsViewModel @Inject constructor(
    private val getAllTripsUseCase: GetAllTripsUseCase,
    private val deleteTripUseCase: DeleteTripUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyTripsUiState())
    val uiState: StateFlow<MyTripsUiState> = _uiState.asStateFlow()

    init {
        loadTrips()
    }

    private fun loadTrips() {
        viewModelScope.launch {
            getAllTripsUseCase().collect { trips ->
                val today = LocalDate.now()
                val upcoming = trips.filter { it.status == TripStatus.UPCOMING }
                val ongoing = trips.filter { trip ->
                    trip.status == TripStatus.ONGOING ||
                            (!today.isBefore(trip.startDate) && !today.isAfter(trip.endDate))
                }
                val completed = trips.filter { it.status == TripStatus.COMPLETED }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        upcomingTrips = upcoming,
                        ongoingTrips = ongoing,
                        completedTrips = completed
                    )
                }
            }
        }
    }

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            deleteTripUseCase(trip)
        }
    }
}
