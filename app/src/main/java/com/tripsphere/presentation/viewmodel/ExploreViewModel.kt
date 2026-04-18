package com.tripsphere.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tripsphere.domain.model.Destination
import com.tripsphere.domain.model.DestinationCategory
import com.tripsphere.domain.repository.PlacesRepository
import com.tripsphere.utils.DummyData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExploreUiState(
    val searchQuery: String = "",
    val selectedCategory: DestinationCategory = DestinationCategory.ALL,
    val allDestinations: List<Destination> = emptyList(),
    val filteredDestinations: List<Destination> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        loadDestinations(DestinationCategory.ALL)
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun onCategorySelected(category: DestinationCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadDestinations(category)
    }

    fun shuffleDestinations() {
        _uiState.update { it.copy(filteredDestinations = it.filteredDestinations.shuffled()) }
    }

    fun refresh() = loadDestinations(_uiState.value.selectedCategory)

    private fun loadDestinations(category: DestinationCategory) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            placesRepository.fetchTouristPlaces(category)
                .onSuccess { places ->
                    val destinations = if (places.isEmpty()) DummyData.destinations else places
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            allDestinations = destinations,
                            filteredDestinations = applyQueryFilter(destinations, it.searchQuery)
                        )
                    }
                }
                .onFailure { err ->
                    // Fall back to local dummy data so the screen is never empty
                    val fallback = DummyData.destinations.filter {
                        category == DestinationCategory.ALL || it.category == category
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            allDestinations = fallback,
                            filteredDestinations = applyQueryFilter(fallback, it.searchQuery),
                            error = "Could not fetch live data: ${err.message}"
                        )
                    }
                }
        }
    }

    private fun applyFilters() {
        val state = _uiState.value
        _uiState.update {
            it.copy(filteredDestinations = applyQueryFilter(state.allDestinations, state.searchQuery))
        }
    }

    private fun applyQueryFilter(destinations: List<Destination>, query: String): List<Destination> {
        if (query.isEmpty()) return destinations
        return destinations.filter { dest ->
            dest.name.contains(query, ignoreCase = true) ||
                dest.country.contains(query, ignoreCase = true) ||
                dest.description.contains(query, ignoreCase = true)
        }
    }
}
