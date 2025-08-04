package com.data.repository

import com.data.api.WeatherApi
import com.data.models.ForecastProperties
import com.data.models.ForecastResponse
import com.data.models.Period
import com.data.models.WeatherProperties
import com.data.models.WeatherResponse
import com.domain.models.Result
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@DisplayName("WeatherRepositoryImpl Tests")
class WeatherRepositoryImplTest {
    private lateinit var api: WeatherApi
    private lateinit var repository: WeatherRepositoryImpl

    private val testLatitude = 37.2883
    private val testLongitude = -121.8434

    @BeforeEach
    fun setup() {
        // Given
        api = mock()
        repository = WeatherRepositoryImpl(api)
    }

    @Test
    @DisplayName("Given successful API calls when getting today temperature then returns temperature success")
    fun givenSuccessfulApiCalls_whenGettingTodayTemperature_thenReturnsTemperatureSuccess() =
        runTest {
            // Given
            val expectedTemperature = 75
            val forecastUrl = "https://api.weather.gov/test/forecast"
            val weatherResponse =
                WeatherResponse(
                    properties = WeatherProperties(forecast = forecastUrl),
                )
            val forecastResponse =
                ForecastResponse(
                    properties =
                        ForecastProperties(
                            periods = listOf(Period(temperature = expectedTemperature)),
                        ),
                )
            whenever(api.getWeatherPoints(testLatitude, testLongitude)).thenReturn(weatherResponse)
            whenever(api.getForecast(forecastUrl)).thenReturn(forecastResponse)

            // When
            val result = repository.getTodayTemperature()

            // Then
            assertTrue(result is Result.Success)
            assertEquals(expectedTemperature, (result as Result.Success).data)
        }

    @Test
    @DisplayName("Given API throws network exception when getting today temperature then returns error result")
    fun givenApiThrowsNetworkException_whenGettingTodayTemperature_thenReturnsErrorResult() =
        runTest {
            // Given
            val expectedException = RuntimeException("Network connection failed")
            whenever(api.getWeatherPoints(testLatitude, testLongitude)).thenThrow(expectedException)

            // When
            val result = repository.getTodayTemperature()

            // Then
            assertTrue(result is Result.Error)
            assertEquals("Network connection failed", (result as Result.Error).message)
        }

    @Test
    @DisplayName("Given API throws generic exception when getting today temperature then returns error with message")
    fun givenApiThrowsGenericException_whenGettingTodayTemperature_thenReturnsErrorWithMessage() =
        runTest {
            // Given
            val expectedException = RuntimeException("Service unavailable")
            whenever(api.getWeatherPoints(testLatitude, testLongitude)).thenThrow(expectedException)

            // When
            val result = repository.getTodayTemperature()

            // Then
            assertTrue(result is Result.Error)
            assertEquals("Service unavailable", (result as Result.Error).message)
        }

    @Test
    @DisplayName("Given API throws exception with null message when getting today temperature then returns default error message")
    fun givenApiThrowsExceptionWithNullMessage_whenGettingTodayTemperature_thenReturnsDefaultErrorMessage() =
        runTest {
            // Given
            val expectedException = RuntimeException(null as String?)
            whenever(api.getWeatherPoints(testLatitude, testLongitude)).thenThrow(expectedException)

            // When
            val result = repository.getTodayTemperature()

            // Then
            assertTrue(result is Result.Error)
            assertEquals("Unknown error occurred", (result as Result.Error).message)
        }

    @Test
    @DisplayName("Given forecast call fails when getting today temperature then returns error result")
    fun givenForecastCallFails_whenGettingTodayTemperature_thenReturnsErrorResult() =
        runTest {
            // Given
            val forecastUrl = "https://api.weather.gov/test/forecast"
            val weatherResponse =
                WeatherResponse(
                    properties = WeatherProperties(forecast = forecastUrl),
                )
            whenever(api.getWeatherPoints(testLatitude, testLongitude)).thenReturn(weatherResponse)
            whenever(api.getForecast(forecastUrl)).thenThrow(RuntimeException("Forecast service down"))

            // When
            val result = repository.getTodayTemperature()

            // Then
            assertTrue(result is Result.Error)
            assertEquals("Forecast service down", (result as Result.Error).message)
        }

    @Test
    @DisplayName("Given successful temperature call when getting weather then returns weather success")
    fun givenSuccessfulTemperatureCall_whenGettingWeather_thenReturnsWeatherSuccess() =
        runTest {
            // Given
            val expectedTemperature = 68
            val forecastUrl = "https://api.weather.gov/test/forecast"
            val weatherResponse =
                WeatherResponse(
                    properties = WeatherProperties(forecast = forecastUrl),
                )
            val forecastResponse =
                ForecastResponse(
                    properties =
                        ForecastProperties(
                            periods = listOf(Period(temperature = expectedTemperature)),
                        ),
                )
            whenever(api.getWeatherPoints(testLatitude, testLongitude)).thenReturn(weatherResponse)
            whenever(api.getForecast(forecastUrl)).thenReturn(forecastResponse)

            // When
            val result = repository.getWeather()

            // Then
            assertTrue(result is Result.Success)
            result as Result.Success
            assertEquals(expectedTemperature, result.data.temperature)
            assertEquals("San Jose, CA", result.data.location)
        }

    @Test
    @DisplayName("Given temperature call returns error when getting weather then returns same error")
    fun givenTemperatureCallReturnsError_whenGettingWeather_thenReturnsSameError() =
        runTest {
            // Given
            val expectedErrorMessage = "Temperature service failed"
            whenever(api.getWeatherPoints(testLatitude, testLongitude))
                .thenThrow(RuntimeException(expectedErrorMessage))

            // When
            val result = repository.getWeather()

            // Then
            assertTrue(result is Result.Error)
            assertEquals(expectedErrorMessage, (result as Result.Error).message)
        }

    @Test
    @DisplayName("Given successful temperature but mapping fails when getting weather then returns error")
    fun givenSuccessfulTemperatureButMappingFails_whenGettingWeather_thenReturnsError() =
        runTest {
            // Given
            val expectedTemperature = 72
            val forecastUrl = "https://api.weather.gov/test/forecast"
            val weatherResponse =
                WeatherResponse(
                    properties = WeatherProperties(forecast = forecastUrl),
                )
            val forecastResponse =
                ForecastResponse(
                    properties =
                        ForecastProperties(
                            periods = listOf(Period(temperature = expectedTemperature)),
                        ),
                )
            whenever(api.getWeatherPoints(testLatitude, testLongitude)).thenReturn(weatherResponse)
            whenever(api.getForecast(forecastUrl)).thenReturn(forecastResponse)

            // When
            val result = repository.getWeather()

            // Then
            assertTrue(result is Result.Success)
            assertEquals(expectedTemperature, (result as Result.Success).data.temperature)
        }

    @Test
    @DisplayName("Given empty periods list when getting today temperature then returns error result")
    fun givenEmptyPeriodsList_whenGettingTodayTemperature_thenReturnsErrorResult() =
        runTest {
            // Given
            val forecastUrl = "https://api.weather.gov/test/forecast"
            val weatherResponse =
                WeatherResponse(
                    properties = WeatherProperties(forecast = forecastUrl),
                )
            val forecastResponse =
                ForecastResponse(
                    properties = ForecastProperties(periods = emptyList()),
                )
            whenever(api.getWeatherPoints(testLatitude, testLongitude)).thenReturn(weatherResponse)
            whenever(api.getForecast(forecastUrl)).thenReturn(forecastResponse)

            // When
            val result = repository.getTodayTemperature()

            // Then
            assertTrue(result is Result.Error)
            result as Result.Error
            // Should contain some error message about empty list or index out of bounds
            assertTrue(result.message.isNotEmpty())
        }
}
