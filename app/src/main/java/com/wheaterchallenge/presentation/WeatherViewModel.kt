package com.wheaterchallenge.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.models.Result
import com.domain.usecase.GetTodayTemperatureUseCase
import com.domain.usecase.GetWeatherDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getTodayTemperatureUseCase: GetTodayTemperatureUseCase,
    private val getWeatherDataUseCase: GetWeatherDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        loadTemperature()
    }

    fun loadTemperature() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, isError = false)
            
            when (val result = getWeatherDataUseCase()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        temperature = result.data.temperature,
                        shortForecast = result.data.shortForecast,
                        icon = result.data.icon,
                        isLoading = false,
                        isError = false
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = result.message
                    )
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true, isError = false)
                }
            }
        }
    }

    fun retry() {
        loadTemperature()
    }
}

data class WeatherUiState(
    val temperature: Int = 0,
    val shortForecast: String = "",
    val icon: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)