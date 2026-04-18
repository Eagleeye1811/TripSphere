package com.tripsphere.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tripsphere.domain.model.Hotel
import com.tripsphere.domain.repository.HotelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HotelUiState(
    val hotels: List<Hotel> = emptyList(),
    val filtered: List<Hotel> = emptyList(),
    val typeFilter: String = "All",
    val sortBy: String = "Distance",           // "Distance" | "Stars" | "Name"
    val isLoading: Boolean = false,
    val error: String? = null,
    val fromCache: Boolean = false,
    val radiusKm: Int = 5
)

// Keep legacy sealed class so existing code compiles during migration
sealed class HotelState {
    object Idle    : HotelState()
    object Loading : HotelState()
    data class Success(val hotels: List<Hotel>) : HotelState()
    data class Error(val message: String)       : HotelState()
}

@HiltViewModel
class HotelViewModel @Inject constructor(
    private val hotelRepository: HotelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HotelUiState())
    val uiState: StateFlow<HotelUiState> = _uiState.asStateFlow()

    private var lastLat = 0.0
    private var lastLon = 0.0

    // ── public API ────────────────────────────────────────────────────────────

    fun loadHotels(latitude: Double, longitude: Double) {
        if (latitude == 0.0 && longitude == 0.0) {
            _uiState.update { it.copy(error = "No location data for this destination.", isLoading = false) }
            return
        }
        lastLat = latitude
        lastLon = longitude
        fetchHotels(latitude, longitude, radiusMeters = 5_000)
    }

    fun refresh() {
        if (lastLat == 0.0 && lastLon == 0.0) return
        fetchHotels(lastLat, lastLon, radiusMeters = _uiState.value.radiusKm * 1_000)
    }

    fun setTypeFilter(type: String) {
        _uiState.update { state ->
            val filtered = applyFilters(state.hotels, type, state.sortBy)
            state.copy(typeFilter = type, filtered = filtered)
        }
    }

    fun setSortBy(sort: String) {
        _uiState.update { state ->
            val filtered = applyFilters(state.hotels, state.typeFilter, sort)
            state.copy(sortBy = sort, filtered = filtered)
        }
    }

    // ── private ───────────────────────────────────────────────────────────────

    private fun fetchHotels(lat: Double, lon: Double, radiusMeters: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            hotelRepository.getNearbyHotels(lat, lon, radiusMeters)
                .onSuccess { hotels ->
                    if (hotels.isEmpty() && radiusMeters < 15_000) {
                        // Auto-expand to 15 km if nothing nearby
                        fetchHotels(lat, lon, 15_000)
                        return@onSuccess
                    }
                    val state = _uiState.value
                    val filtered = applyFilters(hotels, state.typeFilter, state.sortBy)
                    _uiState.update {
                        it.copy(
                            hotels     = hotels,
                            filtered   = filtered,
                            isLoading  = false,
                            error      = if (hotels.isEmpty()) "No hotels found within 15 km." else null,
                            fromCache  = false,
                            radiusKm   = radiusMeters / 1_000
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Failed to load hotels.") }
                }
        }
    }

    private fun applyFilters(hotels: List<Hotel>, type: String, sort: String): List<Hotel> {
        val byType = if (type == "All") hotels
                     else hotels.filter { it.type.lowercase() == type.lowercase() }
        return when (sort) {
            "Stars"    -> byType.sortedByDescending { it.stars ?: 0 }
            "Name"     -> byType.sortedBy { it.name }
            "Price"    -> byType.sortedBy { priceKey(it.estimatedPriceRange) }
            else       -> byType.sortedBy { it.distanceMeters }
        }
    }

    private fun priceKey(range: String): Int {
        val digits = range.filter { it.isDigit() || it == ',' }
            .split(",").firstOrNull()?.replace(",", "") ?: "99999"
        return digits.toIntOrNull() ?: 99999
    }
}
