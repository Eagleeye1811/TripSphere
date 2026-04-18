package com.tripsphere.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tripsphere.domain.model.Place
import com.tripsphere.domain.model.PlaceSuggestion
import com.tripsphere.domain.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class NearbyPlacesState {
    object Idle : NearbyPlacesState()
    object Loading : NearbyPlacesState()
    data class Success(val places: List<Place>) : NearbyPlacesState()
    data class Error(val message: String) : NearbyPlacesState()
}

@OptIn(FlowPreview::class)
@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val _nearbyState = MutableStateFlow<NearbyPlacesState>(NearbyPlacesState.Idle)
    val nearbyState: StateFlow<NearbyPlacesState> = _nearbyState.asStateFlow()

    private val _suggestions = MutableStateFlow<List<PlaceSuggestion>>(emptyList())
    val suggestions: StateFlow<List<PlaceSuggestion>> = _suggestions.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private var currentLat: Double = 0.0
    private var currentLon: Double = 0.0

    init {
        _searchQuery
            .debounce(400L)
            .filter { it.length >= 2 }
            .onEach { query -> fetchSuggestions(query) }
            .launchIn(viewModelScope)
    }

    fun loadNearbyPlaces(latitude: Double, longitude: Double, radiusMeters: Int = 3000) {
        currentLat = latitude
        currentLon = longitude
        viewModelScope.launch {
            _nearbyState.value = NearbyPlacesState.Loading
            placesRepository.getNearbyPlaces(latitude, longitude, radiusMeters)
                .onSuccess { _nearbyState.value = NearbyPlacesState.Success(it) }
                .onFailure { e ->
                    _nearbyState.value = NearbyPlacesState.Error(
                        if (e.message?.contains("401") == true)
                            "Foursquare API key not configured. Add your key in build.gradle.kts."
                        else e.message ?: "Failed to load nearby places"
                    )
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) _suggestions.value = emptyList()
    }

    fun clearSuggestions() {
        _suggestions.value = emptyList()
        _searchQuery.value = ""
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    private suspend fun fetchSuggestions(query: String) {
        placesRepository.autocomplete(
            query = query,
            latitude = currentLat.takeIf { it != 0.0 },
            longitude = currentLon.takeIf { it != 0.0 }
        ).onSuccess { _suggestions.value = it }
    }
}
