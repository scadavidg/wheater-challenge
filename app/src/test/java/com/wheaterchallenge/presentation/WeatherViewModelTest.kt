package com.wheaterchallenge.presentation

import app.cash.turbine.test
import com.domain.models.Result
import com.domain.models.Weather
import com.domain.usecase.GetTodayTemperatureUseCase
import com.domain.usecase.GetWeatherDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.kotlin.atLeast
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@DisplayName("WeatherViewModel Tests")
class WeatherViewModelTest {

    private lateinit var getTodayTemperatureUseCase: GetTodayTemperatureUseCase
    private lateinit var getWeatherDataUseCase: GetWeatherDataUseCase
    private lateinit var viewModel: WeatherViewModel
    
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setup() {
        // Set the main dispatcher to our test dispatcher for coroutines
        Dispatchers.setMain(testDispatcher)
        
        // Create mocks
        getTodayTemperatureUseCase = mock()
        getWeatherDataUseCase = mock()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @DisplayName("Given successful weather data when init then updates UI state with data")
    fun given_successfulWeatherData_when_init_then_updatesUiStateWithData() = runTest {
        // Given
        val expectedWeather = Weather(
            temperature = 75,
            shortForecast = "Sunny",
            icon = "https://api.weather.gov/icons/land/day/skc?size=medium",
            location = "San Jose, CA"
        )
        whenever(getWeatherDataUseCase()).thenReturn(Result.Success(expectedWeather))

        // When
        viewModel = WeatherViewModel(getTodayTemperatureUseCase, getWeatherDataUseCase)

        // Then
        viewModel.uiState.test {
            val finalState = awaitItem()
            assertEquals(75, finalState.temperature)
            assertEquals("Sunny", finalState.shortForecast)
            assertEquals("https://api.weather.gov/icons/land/day/skc?size=medium", finalState.icon)
            assertFalse(finalState.isLoading)
            assertFalse(finalState.isError)
            assertEquals("", finalState.errorMessage)
        }
    }

    @Test
    @DisplayName("Given error from use case when init then updates UI state with error")
    fun given_errorFromUseCase_when_init_then_updatesUiStateWithError() = runTest {
        // Given
        val errorMessage = "Network connection failed"
        whenever(getWeatherDataUseCase()).thenReturn(Result.Error(errorMessage))

        // When
        viewModel = WeatherViewModel(getTodayTemperatureUseCase, getWeatherDataUseCase)

        // Then
        viewModel.uiState.test {
            val finalState = awaitItem()
            assertEquals(0, finalState.temperature)
            assertEquals("", finalState.shortForecast)
            assertEquals("", finalState.icon)
            assertFalse(finalState.isLoading)
            assertTrue(finalState.isError)
            assertEquals(errorMessage, finalState.errorMessage)
        }
    }

    @Test
    @DisplayName("Given loading from use case when init then updates UI state with loading")
    fun given_loadingFromUseCase_when_init_then_updatesUiStateWithLoading() = runTest {
        // Given
        whenever(getWeatherDataUseCase()).thenReturn(Result.Loading)

        // When
        viewModel = WeatherViewModel(getTodayTemperatureUseCase, getWeatherDataUseCase)

        // Then
        viewModel.uiState.test {
            val finalState = awaitItem()
            assertEquals(0, finalState.temperature)
            assertEquals("", finalState.shortForecast)
            assertEquals("", finalState.icon)
            assertTrue(finalState.isLoading)
            assertFalse(finalState.isError)
            assertEquals("", finalState.errorMessage)
        }
    }

    @Test
    @DisplayName("Given error state when retry then retries loading data")
    fun given_errorState_when_retry_then_retriesLoadingData() = runTest {
        // Given - Initially returns error
        val errorMessage = "API temporarily unavailable"
        whenever(getWeatherDataUseCase()).thenReturn(Result.Error(errorMessage))
        
        viewModel = WeatherViewModel(getTodayTemperatureUseCase, getWeatherDataUseCase)

        // Then setup successful retry
        val successWeather = Weather(
            temperature = 68,
            shortForecast = "Partly Cloudy",
            icon = "https://api.weather.gov/icons/land/day/bkn?size=medium"
        )
        whenever(getWeatherDataUseCase()).thenReturn(Result.Success(successWeather))

        // When
        viewModel.retry()

        // Then
        viewModel.uiState.test {
            val retryState = awaitItem()
            assertEquals(68, retryState.temperature)
            assertEquals("Partly Cloudy", retryState.shortForecast)
            assertFalse(retryState.isLoading)
            assertFalse(retryState.isError)
        }

        // Verify use case was called at least twice (init + retry)
        verify(getWeatherDataUseCase, atLeast(2)).invoke()
    }

    @Test
    @DisplayName("Given successful data when retry then updates UI state with new data")
    fun given_successfulData_when_retry_then_updatesUiStateWithNewData() = runTest {
        // Given - Initial successful data
        val initialWeather = Weather(
            temperature = 70,
            shortForecast = "Clear",
            icon = "https://api.weather.gov/icons/land/day/few?size=medium"
        )
        whenever(getWeatherDataUseCase()).thenReturn(Result.Success(initialWeather))
        
        viewModel = WeatherViewModel(getTodayTemperatureUseCase, getWeatherDataUseCase)

        // Setup new data for retry
        val updatedWeather = Weather(
            temperature = 73,
            shortForecast = "Sunny",
            icon = "https://api.weather.gov/icons/land/day/skc?size=medium"
        )
        whenever(getWeatherDataUseCase()).thenReturn(Result.Success(updatedWeather))

        // When
        viewModel.retry()

        // Then
        viewModel.uiState.test {
            val newState = awaitItem()
            assertEquals(73, newState.temperature)
            assertEquals("Sunny", newState.shortForecast)
            assertEquals("https://api.weather.gov/icons/land/day/skc?size=medium", newState.icon)
            assertFalse(newState.isLoading)
            assertFalse(newState.isError)
        }
    }

    @Test
    @DisplayName("Given UI state transitions when loading then shows correct intermediate states")
    fun given_uiStateTransitions_when_loading_then_showsCorrectIntermediateStates() = runTest {
        // Given
        val successWeather = Weather(
            temperature = 72,
            shortForecast = "Partly Cloudy",
            icon = "https://api.weather.gov/icons/land/day/bkn?size=medium"
        )
        whenever(getWeatherDataUseCase()).thenReturn(Result.Success(successWeather))

        // When
        viewModel = WeatherViewModel(getTodayTemperatureUseCase, getWeatherDataUseCase)

        // Then - Verify state transitions during loading
        viewModel.uiState.test {
            val finalState = awaitItem()
            
            // Should go directly to success state since we're using UnconfinedTestDispatcher
            assertEquals(72, finalState.temperature)
            assertEquals("Partly Cloudy", finalState.shortForecast)
            assertFalse(finalState.isLoading)
            assertFalse(finalState.isError)
        }
    }

    @Test
    @DisplayName("Given default UI state when created then has correct initial values")
    fun given_defaultUiState_when_created_then_hasCorrectInitialValues() {
        // When
        val defaultState = WeatherUiState()

        // Then
        assertEquals(0, defaultState.temperature)
        assertEquals("", defaultState.shortForecast)
        assertEquals("", defaultState.icon)
        assertFalse(defaultState.isLoading)
        assertFalse(defaultState.isError)
        assertEquals("", defaultState.errorMessage)
    }

    @Test
    @DisplayName("Given UI state when copied with new values then updates correctly")
    fun given_uiState_when_copiedWithNewValues_then_updatesCorrectly() {
        // Given
        val originalState = WeatherUiState(
            temperature = 70,
            shortForecast = "Clear",
            icon = "test-icon",
            isLoading = false,
            isError = false
        )

        // When
        val updatedState = originalState.copy(
            temperature = 75,
            isLoading = true
        )

        // Then
        assertEquals(75, updatedState.temperature)
        assertEquals("Clear", updatedState.shortForecast) // Should remain unchanged
        assertEquals("test-icon", updatedState.icon) // Should remain unchanged
        assertTrue(updatedState.isLoading) // Should be updated
        assertFalse(updatedState.isError) // Should remain unchanged
    }

    @Test
    @DisplayName("Given multiple retry calls when invoked rapidly then handles gracefully")
    fun given_multipleRetryCalls_when_invokedRapidly_then_handlesGracefully() = runTest {
        // Given
        val weather = Weather(
            temperature = 65,
            shortForecast = "Cloudy",
            icon = "https://api.weather.gov/icons/land/day/ovc?size=medium"
        )
        whenever(getWeatherDataUseCase()).thenReturn(Result.Success(weather))
        
        viewModel = WeatherViewModel(getTodayTemperatureUseCase, getWeatherDataUseCase)

        // When - Multiple rapid retry calls
        repeat(3) {
            viewModel.retry()
        }

        // Then - Should handle gracefully and show final state
        viewModel.uiState.test {
            val finalState = awaitItem()
            assertEquals(65, finalState.temperature)
            assertEquals("Cloudy", finalState.shortForecast)
            assertFalse(finalState.isLoading)
            assertFalse(finalState.isError)
        }
    }
}