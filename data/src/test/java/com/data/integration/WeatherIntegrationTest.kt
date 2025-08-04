package com.data.integration

import com.data.api.WeatherApi
import com.data.models.ForecastProperties
import com.data.models.ForecastResponse
import com.data.models.Period
import com.data.models.WeatherProperties
import com.data.models.WeatherResponse
import com.data.repository.WeatherRepositoryImpl
import com.domain.models.Result
import com.domain.usecase.GetTodayTemperatureUseCase
import com.domain.usecase.GetWeatherUseCase
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@DisplayName("Simple Integration Tests")
class WeatherIntegrationTest {
    private lateinit var api: WeatherApi
    private lateinit var repository: WeatherRepositoryImpl
    private lateinit var getTodayTemperatureUseCase: GetTodayTemperatureUseCase
    private lateinit var getWeatherUseCase: GetWeatherUseCase

    @BeforeEach
    fun setup() {
        // Given
        api = mock()
        repository = WeatherRepositoryImpl(api)
        getTodayTemperatureUseCase = GetTodayTemperatureUseCase(repository)
        getWeatherUseCase = GetWeatherUseCase(repository)
    }

    @Test
    @DisplayName("Given successful API integration when calling complete weather flow then returns weather successfully")
    fun givenSuccessfulApiIntegration_whenCallingCompleteWeatherFlow_thenReturnsWeatherSuccessfully() =
        runTest {
            // Given
            val expectedTemperature = 78
            val forecastUrl = "https://api.weather.gov/gridpoints/MTR/85,105/forecast"

            val weatherResponse =
                WeatherResponse(
                    properties = WeatherProperties(forecast = forecastUrl),
                )
            val forecastResponse =
                ForecastResponse(
                    properties =
                        ForecastProperties(
                            periods =
                                listOf(
                                    Period(temperature = expectedTemperature),
                                    Period(temperature = 65),
                                ),
                        ),
                )

            whenever(api.getWeatherPoints(37.2883, -121.8434)).thenReturn(weatherResponse)
            whenever(api.getForecast(forecastUrl)).thenReturn(forecastResponse)

            // When
            val weatherResult = getWeatherUseCase.invoke()

            // Then
            assertTrue(weatherResult is Result.Success)
            weatherResult as Result.Success
            assertEquals(expectedTemperature, weatherResult.data.temperature)
            assertEquals("San Jose, CA", weatherResult.data.location)
        }

    @Test
    @DisplayName("Given API error when calling weather flow then returns error result")
    fun givenApiError_whenCallingWeatherFlow_thenReturnsErrorResult() =
        runTest {
            // Given
            val expectedError = "Service unavailable"
            whenever(api.getWeatherPoints(37.2883, -121.8434)).thenThrow(RuntimeException(expectedError))

            // When
            val result = getTodayTemperatureUseCase.invoke()

            // Then
            assertTrue(result is Result.Error)
            result as Result.Error
            assertEquals(expectedError, result.message)
        }

    @Test
    @DisplayName("Given temperature and weather use cases called independently when both succeed then return consistent results")
    fun givenTemperatureAndWeatherUseCasesCalledIndependently_whenBothSucceed_thenReturnConsistentResults() =
        runTest {
            // Given
            val expectedTemperature = 82
            val forecastUrl = "https://api.weather.gov/gridpoints/MTR/85,105/forecast"

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

            whenever(api.getWeatherPoints(37.2883, -121.8434)).thenReturn(weatherResponse)
            whenever(api.getForecast(forecastUrl)).thenReturn(forecastResponse)

            // When
            val temperatureResult = getTodayTemperatureUseCase.invoke()
            val weatherResult = getWeatherUseCase.invoke()

            // Then
            assertTrue(temperatureResult is Result.Success)
            assertTrue(weatherResult is Result.Success)
            assertEquals(expectedTemperature, (temperatureResult as Result.Success).data)
            assertEquals(expectedTemperature, (weatherResult as Result.Success).data.temperature)
        }

    @Test
    @DisplayName("Given API returns empty periods when calling weather flow then returns error")
    fun givenApiReturnsEmptyPeriods_whenCallingWeatherFlow_thenReturnsError() =
        runTest {
            // Given
            val forecastUrl = "https://api.weather.gov/gridpoints/MTR/85,105/forecast"
            val weatherResponse =
                WeatherResponse(
                    properties = WeatherProperties(forecast = forecastUrl),
                )
            val emptyForecastResponse =
                ForecastResponse(
                    properties = ForecastProperties(periods = emptyList()),
                )

            whenever(api.getWeatherPoints(37.2883, -121.8434)).thenReturn(weatherResponse)
            whenever(api.getForecast(forecastUrl)).thenReturn(emptyForecastResponse)

            // When
            val result = getWeatherUseCase.invoke()

            // Then
            assertTrue(result is Result.Error)
        }

    @Test
    @DisplayName("Given data layer to domain mapping when successful then maps correctly")
    fun givenDataLayerToDomainMapping_whenSuccessful_thenMapsCorrectly() =
        runTest {
            // Given
            val testTemperature = 25
            val forecastUrl = "https://api.weather.gov/test/forecast"

            val weatherResponse =
                WeatherResponse(
                    properties = WeatherProperties(forecast = forecastUrl),
                )
            val forecastResponse =
                ForecastResponse(
                    properties =
                        ForecastProperties(
                            periods = listOf(Period(temperature = testTemperature)),
                        ),
                )

            whenever(api.getWeatherPoints(37.2883, -121.8434)).thenReturn(weatherResponse)
            whenever(api.getForecast(forecastUrl)).thenReturn(forecastResponse)

            // When
            val result = getWeatherUseCase.invoke()

            // Then
            assertTrue(result is Result.Success)
            result as Result.Success

            // Verify that domain model is correctly created
            assertEquals(testTemperature, result.data.temperature)
            assertEquals("San Jose, CA", result.data.location) // Default location

            // Verify it's actually a domain Weather object, not a data object
            assertTrue(result.data is com.domain.models.Weather)
        }
}
