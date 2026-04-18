package com.tripsphere.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tripsphere.domain.model.Trip
import com.tripsphere.domain.model.TripStatus
import com.tripsphere.domain.usecase.GetAllTripsUseCase
import com.tripsphere.domain.usecase.GetCurrentUserUseCase
import com.tripsphere.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val activeTrip: Trip? = null,
    val upcomingTrips: List<Trip> = emptyList(),
    val completedTrips: List<Trip> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllTripsUseCase: GetAllTripsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Load user
            val user = getCurrentUserUseCase()
            _uiState.update { it.copy(user = user) }
        }

        viewModelScope.launch {
            getAllTripsUseCase().catch { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }.collect { trips ->
                val today = LocalDate.now()
                val activeTrip = trips.firstOrNull { trip ->
                    !today.isBefore(trip.startDate) && !today.isAfter(trip.endDate)
                } ?: trips.firstOrNull { it.status == TripStatus.ONGOING }

                val upcoming = trips.filter { it.status == TripStatus.UPCOMING }
                val completed = trips.filter { it.status == TripStatus.COMPLETED }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        activeTrip = activeTrip,
                        upcomingTrips = upcoming,
                        completedTrips = completed
                    )
                }
            }
        }
    }
}
