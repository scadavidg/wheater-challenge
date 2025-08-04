package com.domain.usecase

import com.domain.models.Result
import com.domain.models.Weather
import com.domain.repository.WeatherRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@DisplayName("GetWeatherUseCase Tests")
class GetWeatherUseCaseTest {

    private lateinit var repository: WeatherRepository
    private lateinit var useCase: GetWeatherUseCase

    @BeforeEach
    fun setup() {
        // Given
        repository = mock()
        useCase = GetWeatherUseCase(repository)
    }

    @Test
    @DisplayName("Given repository returns weather success when invoking use case then returns same weather")
    fun givenRepositoryReturnsWeatherSuccess_whenInvokingUseCase_thenReturnsSameWeather() = runTest {
        // Given
        val expectedWeather = Weather(temperature = 22, location = "Test City")
        val successResult = Result.Success(expectedWeather)
        whenever(repository.getWeather()).thenReturn(successResult)

        // When
        val result = useCase.invoke()

        // Then
        assertTrue(result is Result.Success)
        result as Result.Success
        assertEquals(expectedWeather, result.data)
        assertEquals(22, result.data.temperature)
        assertEquals("Test City", result.data.location)
    }

    @Test
    @DisplayName("Given repository returns weather with default location when invoking use case then preserves default location")
    fun givenRepositoryReturnsWeatherWithDefaultLocation_whenInvokingUseCase_thenPreservesDefaultLocation() = runTest {
        // Given
        val expectedWeather = Weather(temperature = 25) // Uses default location
        val successResult = Result.Success(expectedWeather)
        whenever(repository.getWeather()).thenReturn(successResult)

        // When
        val result = useCase.invoke()

        // Then
        assertTrue(result is Result.Success)
        result as Result.Success
        assertEquals(expectedWeather, result.data)
        assertEquals(25, result.data.temperature)
        assertEquals("San Jose, CA", result.data.location)
    }

    @Test
    @DisplayName("Given repository returns error when invoking use case then returns same error")
    fun givenRepositoryReturnsError_whenInvokingUseCase_thenReturnsSameError() = runTest {
        // Given
        val expectedMessage = "Weather service unavailable"
        val errorResult = Result.Error(expectedMessage)
        whenever(repository.getWeather()).thenReturn(errorResult)

        // When
        val result = useCase.invoke()

        // Then
        assertTrue(result is Result.Error)
        result as Result.Error
        assertEquals(expectedMessage, result.message)
    }

    @Test
    @DisplayName("Given repository returns loading when invoking use case then returns same loading")
    fun givenRepositoryReturnsLoading_whenInvokingUseCase_thenReturnsSameLoading() = runTest {
        // Given
        val loadingResult = Result.Loading
        whenever(repository.getWeather()).thenReturn(loadingResult)

        // When
        val result = useCase.invoke()

        // Then
        assertTrue(result is Result.Loading)
        assertEquals(loadingResult, result)
    }

    @Test
    @DisplayName("Given repository throws exception when invoking use case then exception propagates")
    fun givenRepositoryThrowsException_whenInvokingUseCase_thenExceptionPropagates() = runTest {
        // Given
        val expectedException = RuntimeException("Repository connection failed")
        whenever(repository.getWeather()).thenThrow(expectedException)

        // When & Then
        try {
            useCase.invoke()
            throw AssertionError("Expected exception was not thrown")
        } catch (e: RuntimeException) {
            assertEquals(expectedException.message, e.message)
        }
    }
}