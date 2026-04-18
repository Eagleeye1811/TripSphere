package com.tripsphere.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tripsphere.domain.model.Weather
import com.tripsphere.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class WeatherUiState {
    object Idle : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val weather: Weather) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val weatherState: StateFlow<WeatherUiState> = _weatherState.asStateFlow()

    fun loadWeather(latitude: Double, longitude: Double) {
        if (latitude == 0.0 && longitude == 0.0) return
        viewModelScope.launch {
            _weatherState.value = WeatherUiState.Loading
            val result = weatherRepository.getWeather(latitude, longitude)
            _weatherState.value = if (result.isSuccess) {
                WeatherUiState.Success(result.getOrThrow())
            } else {
                WeatherUiState.Error(result.exceptionOrNull()?.message ?: "Could not load weather")
            }
        }
    }
}
