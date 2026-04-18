package com.tripsphere.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tripsphere.domain.model.Itinerary
import com.tripsphere.domain.model.Trip
import com.tripsphere.domain.model.TripStatus
import com.tripsphere.domain.usecase.AddItineraryUseCase
import com.tripsphere.domain.usecase.DeleteItineraryUseCase
import com.tripsphere.domain.usecase.GetItineraryUseCase
import com.tripsphere.domain.usecase.SaveTripUseCase
import com.tripsphere.utils.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class WorkspaceUiState(
    val itineraryByDay: Map<Int, List<Itinerary>> = emptyMap(),
    val notes: String = "",
    val isSaving: Boolean = false,
    val savedTripId: Long? = null,
    val error: String? = null
)

@HiltViewModel
class TripWorkspaceViewModel @Inject constructor(
    private val saveTripUseCase: SaveTripUseCase,
    private val addItineraryUseCase: AddItineraryUseCase,
    private val deleteItineraryUseCase: DeleteItineraryUseCase,
    private val getItineraryUseCase: GetItineraryUseCase,
    private val notificationScheduler: NotificationScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkspaceUiState())
    val uiState: StateFlow<WorkspaceUiState> = _uiState.asStateFlow()

    private val tempItineraries = mutableListOf<Itinerary>()
    private var nextTempId = -1L

    fun updateNotes(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun addItinerary(day: Int, time: String, activity: String, notes: String = "") {
        val item = Itinerary(
            id = nextTempId--,
            tripId = 0,
            day = day,
            time = time,
            activity = activity,
            notes = notes
        )
        tempItineraries.add(item)
        updateItineraryMap()
    }

    fun removeItinerary(itinerary: Itinerary) {
        tempItineraries.removeIf { it.id == itinerary.id }
        updateItineraryMap()
    }

    fun moveItinerary(day: Int, item: Itinerary, direction: Int) {
        val dayItems = tempItineraries.filter { it.day == day }.toMutableList()
        val currentIndex = dayItems.indexOfFirst { it.id == item.id }
        val newIndex = (currentIndex + direction).coerceIn(0, dayItems.size - 1)
        if (currentIndex == newIndex) return

        val globalIndexCurrent = tempItineraries.indexOf(dayItems[currentIndex])
        val globalIndexSwap = tempItineraries.indexOf(dayItems[newIndex])
        if (globalIndexCurrent < 0 || globalIndexSwap < 0) return

        val temp = tempItineraries[globalIndexCurrent]
        tempItineraries[globalIndexCurrent] = tempItineraries[globalIndexSwap]
        tempItineraries[globalIndexSwap] = temp
        updateItineraryMap()
    }

    private fun updateItineraryMap() {
        val grouped = tempItineraries.groupBy { it.day }
        _uiState.update { it.copy(itineraryByDay = grouped) }
    }

    fun saveTrip(
        destination: String,
        startDate: LocalDate,
        endDate: LocalDate,
        budget: Double,
        imageUrl: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val trip = Trip(
                    destination = destination,
                    startDate = startDate,
                    endDate = endDate,
                    budget = budget,
                    imageUrl = imageUrl,
                    status = TripStatus.UPCOMING,
                    notes = _uiState.value.notes
                )
                val tripId = saveTripUseCase(trip)

                tempItineraries.forEach { itinerary ->
                    addItineraryUseCase(itinerary.copy(id = 0, tripId = tripId))
                }

                // Schedule trip departure and itinerary notifications
                notificationScheduler.scheduleTripReminders(tripId, destination, startDate)

                _uiState.update { it.copy(isSaving = false, savedTripId = tripId) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }
}
