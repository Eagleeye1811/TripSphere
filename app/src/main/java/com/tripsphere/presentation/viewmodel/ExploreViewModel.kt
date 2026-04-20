package com.tripsphere.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.tripsphere.domain.model.Destination
import com.tripsphere.domain.model.DestinationCategory
import com.tripsphere.utils.DummyData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
class ExploreViewModel @Inject constructor() : ViewModel() {

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

    private fun loadDestinations(category: DestinationCategory) {
        val all = DummyData.destinations
        val forCategory = if (category == DestinationCategory.ALL) all
                          else all.filter { it.category == category }
        _uiState.update {
            it.copy(
                isLoading = false,
                error = null,
                allDestinations = all,
                filteredDestinations = applyQueryFilter(forCategory, it.searchQuery)
            )
        }
    }

    private fun applyFilters() {
        val state = _uiState.value
        val forCategory = if (state.selectedCategory == DestinationCategory.ALL)
            state.allDestinations
        else
            state.allDestinations.filter { it.category == state.selectedCategory }
        _uiState.update {
            it.copy(filteredDestinations = applyQueryFilter(forCategory, state.searchQuery))
        }
    }

    private fun applyQueryFilter(destinations: List<Destination>, query: String): List<Destination> {
        if (query.isEmpty()) return destinations
        return destinations.filter { dest ->
            dest.name.contains(query, ignoreCase = true) ||
                dest.country.contains(query, ignoreCase = true) ||
                dest.description.contains(query, ignoreCase = true) ||
                dest.highlights.contains(query, ignoreCase = true)
        }
    }
}
