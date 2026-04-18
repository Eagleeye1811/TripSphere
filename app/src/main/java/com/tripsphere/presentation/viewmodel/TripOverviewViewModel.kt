package com.tripsphere.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tripsphere.domain.model.Itinerary
import com.tripsphere.domain.model.Trip
import com.tripsphere.domain.model.TripStatus
import com.tripsphere.domain.usecase.DeleteTripUseCase
import com.tripsphere.domain.usecase.GetItineraryUseCase
import com.tripsphere.domain.usecase.GetTripByIdUseCase
import com.tripsphere.domain.usecase.UpdateTripUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TripOverviewUiState(
    val isLoading: Boolean = true,
    val trip: Trip? = null,
    val itineraries: List<Itinerary> = emptyList(),
    val tripStarted: Boolean = false,
    val tripDeleted: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TripOverviewViewModel @Inject constructor(
    private val getTripByIdUseCase: GetTripByIdUseCase,
    private val getItineraryUseCase: GetItineraryUseCase,
    private val updateTripUseCase: UpdateTripUseCase,
    private val deleteTripUseCase: DeleteTripUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripOverviewUiState())
    val uiState: StateFlow<TripOverviewUiState> = _uiState.asStateFlow()

    /** Mark trip as ONGOING and signal the UI to navigate to MyTrips. */
    fun startTrip() {
        val trip = _uiState.value.trip ?: return
        viewModelScope.launch {
            updateTripUseCase(trip.copy(status = TripStatus.ONGOING))
            _uiState.update { it.copy(tripStarted = true) }
        }
    }

    /** Permanently delete this trip and signal the UI to navigate back. */
    fun deleteTrip() {
        val trip = _uiState.value.trip ?: return
        viewModelScope.launch {
            deleteTripUseCase(trip)
            _uiState.update { it.copy(tripDeleted = true) }
        }
    }

    fun loadTrip(tripId: Long) {
        viewModelScope.launch {
            try {
                val trip = getTripByIdUseCase(tripId)
                _uiState.update { it.copy(trip = trip, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
        viewModelScope.launch {
            getItineraryUseCase(tripId).collect { itineraries ->
                _uiState.update { it.copy(itineraries = itineraries) }
            }
        }
    }
}
