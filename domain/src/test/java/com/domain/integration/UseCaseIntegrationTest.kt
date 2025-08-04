package com.domain.integration

import com.domain.models.Result
import com.domain.models.Weather
import com.domain.repository.WeatherRepository
import com.domain.usecase.GetTodayTemperatureUseCase
import com.domain.usecase.GetWeatherUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@DisplayName("Use Case Integration Tests")
class UseCaseIntegrationTest {

    private lateinit var repository: WeatherRepository
    private lateinit var getTodayTemperatureUseCase: GetTodayTemperatureUseCase
    private lateinit var getWeatherUseCase: GetWeatherUseCase

    @BeforeEach
    fun setup() {
        // Given
        repository = mock()
        getTodayTemperatureUseCase = GetTodayTemperatureUseCase(repository)
        getWeatherUseCase = GetWeatherUseCase(repository)
    }

    @Test
    @DisplayName("Given repository works correctly when calling both use cases sequentially then both return success")
    fun givenRepositoryWorksCorrectly_whenCallingBothUseCasesSequentially_thenBothReturnSuccess() = runTest {
        // Given
        val expectedTemperature = 75
        val expectedWeather = Weather(temperature = expectedTemperature, location = "Test City")
        whenever(repository.getTodayTemperature()).thenReturn(Result.Success(expectedTemperature))
        whenever(repository.getWeather()).thenReturn(Result.Success(expectedWeather))

        // When
        val temperatureResult = getTodayTemperatureUseCase.invoke()
        val weatherResult = getWeatherUseCase.invoke()

        // Then
        assertTrue(temperatureResult is Result.Success)
        assertTrue(weatherResult is Result.Success)
        assertEquals(expectedTemperature, (temperatureResult as Result.Success).data)
        assertEquals(expectedWeather, (weatherResult as Result.Success).data)
    }

    @Test
    @DisplayName("Given repository fails when calling both use cases then both return same error")
    fun givenRepositoryFails_whenCallingBothUseCases_thenBothReturnSameError() = runTest {
        // Given
        val expectedError = "Service temporarily unavailable"
        whenever(repository.getTodayTemperature()).thenReturn(Result.Error(expectedError))
        whenever(repository.getWeather()).thenReturn(Result.Error(expectedError))

        // When
        val temperatureResult = getTodayTemperatureUseCase.invoke()
        val weatherResult = getWeatherUseCase.invoke()

        // Then
        assertTrue(temperatureResult is Result.Error)
        assertTrue(weatherResult is Result.Error)
        assertEquals(expectedError, (temperatureResult as Result.Error).message)
        assertEquals(expectedError, (weatherResult as Result.Error).message)
    }

    @Test
    @DisplayName("Given concurrent use case calls when repository responds then handles concurrency correctly")
    fun givenConcurrentUseCaseCalls_whenRepositoryResponds_thenHandlesConcurrencyCorrectly() = runTest {
        // Given
        val expectedTemperature = 68
        val expectedWeather = Weather(temperature = expectedTemperature, location = "Concurrent City")
        whenever(repository.getTodayTemperature()).thenReturn(Result.Success(expectedTemperature))
        whenever(repository.getWeather()).thenReturn(Result.Success(expectedWeather))

        // When
        val temperatureDeferred = async { getTodayTemperatureUseCase.invoke() }
        val weatherDeferred = async { getWeatherUseCase.invoke() }
        
        val temperatureResult = temperatureDeferred.await()
        val weatherResult = weatherDeferred.await()

        // Then
        assertTrue(temperatureResult is Result.Success)
        assertTrue(weatherResult is Result.Success)
        assertEquals(expectedTemperature, (temperatureResult as Result.Success).data)
        assertEquals(expectedWeather, (weatherResult as Result.Success).data)
    }

    @Test
    @DisplayName("Given repository returns loading when calling use cases then preserve loading state")
    fun givenRepositoryReturnsLoading_whenCallingUseCases_thenPreserveLoadingState() = runTest {
        // Given
        whenever(repository.getTodayTemperature()).thenReturn(Result.Loading)
        whenever(repository.getWeather()).thenReturn(Result.Loading)

        // When
        val temperatureResult = getTodayTemperatureUseCase.invoke()
        val weatherResult = getWeatherUseCase.invoke()

        // Then
        assertTrue(temperatureResult is Result.Loading)
        assertTrue(weatherResult is Result.Loading)
        assertEquals(Result.Loading, temperatureResult)
        assertEquals(Result.Loading, weatherResult)
    }

    @Test
    @DisplayName("Given mixed repository responses when calling use cases then each returns appropriate result")
    fun givenMixedRepositoryResponses_whenCallingUseCases_thenEachReturnsAppropriateResult() = runTest {
        // Given
        val expectedTemperature = 80
        val expectedError = "Weather data not available"
        whenever(repository.getTodayTemperature()).thenReturn(Result.Success(expectedTemperature))
        whenever(repository.getWeather()).thenReturn(Result.Error(expectedError))

        // When
        val temperatureResult = getTodayTemperatureUseCase.invoke()
        val weatherResult = getWeatherUseCase.invoke()

        // Then
        assertTrue(temperatureResult is Result.Success)
        assertTrue(weatherResult is Result.Error)
        assertEquals(expectedTemperature, (temperatureResult as Result.Success).data)
        assertEquals(expectedError, (weatherResult as Result.Error).message)
    }

    @Test
    @DisplayName("Given multiple sequential calls when repository state changes then reflects current state")
    fun givenMultipleSequentialCalls_whenRepositoryStateChanges_thenReflectsCurrentState() = runTest {
        // Given - First call succeeds
        val firstTemperature = 72
        whenever(repository.getTodayTemperature()).thenReturn(Result.Success(firstTemperature))

        // When - First call
        val firstResult = getTodayTemperatureUseCase.invoke()

        // Given - Second call fails
        val errorMessage = "Network timeout"
        whenever(repository.getTodayTemperature()).thenReturn(Result.Error(errorMessage))

        // When - Second call
        val secondResult = getTodayTemperatureUseCase.invoke()

        // Then
        assertTrue(firstResult is Result.Success)
        assertEquals(firstTemperature, (firstResult as Result.Success).data)
        assertTrue(secondResult is Result.Error)
        assertEquals(errorMessage, (secondResult as Result.Error).message)
    }
}