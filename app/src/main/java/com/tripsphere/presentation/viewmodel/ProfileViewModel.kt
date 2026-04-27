package com.tripsphere.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tripsphere.domain.model.User
import com.tripsphere.domain.repository.AuthRepository
import com.tripsphere.domain.usecase.GetAllTripsUseCase
import com.tripsphere.utils.FavouritesStore
import com.tripsphere.utils.OnboardingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: User? = null,
    val totalTrips: Int = 0,
    val placesVisited: Int = 0,
    val totalBudget: Double = 0.0,
    val isLoading: Boolean = true
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getAllTripsUseCase: GetAllTripsUseCase,
    private val onboardingPreferences: OnboardingPreferences,
    private val favouritesStore: FavouritesStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    val favouriteCount: StateFlow<Int> = favouritesStore.favouriteIds
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    init {
        loadProfile()
    }

    private fun loadProfile() {
        val firebaseUser = User(
            name = authRepository.getCurrentUserName() ?: "Traveler",
            email = authRepository.getCurrentUserEmail() ?: ""
        )
        _uiState.update { it.copy(user = firebaseUser) }

        viewModelScope.launch {
            getAllTripsUseCase().collect { trips ->
                val destinations = trips.map { it.destination }.distinct()
                _uiState.update {
                    it.copy(
                        totalTrips = trips.size,
                        placesVisited = destinations.size,
                        totalBudget = trips.sumOf { t -> t.budget },
                        isLoading = false
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.signOut()
            onboardingPreferences.setLoggedIn(false)
        }
    }
}
