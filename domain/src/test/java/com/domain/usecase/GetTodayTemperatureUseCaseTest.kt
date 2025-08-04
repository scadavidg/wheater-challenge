package com.domain.usecase

import com.domain.models.Result
import com.domain.repository.WeatherRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@DisplayName("GetTodayTemperatureUseCase Tests")
class GetTodayTemperatureUseCaseTest {

    private lateinit var repository: WeatherRepository
    private lateinit var useCase: GetTodayTemperatureUseCase

    @BeforeEach
    fun setup() {
        // Given
        repository = mock()
        useCase = GetTodayTemperatureUseCase(repository)
    }

    @Test
    @DisplayName("Given repository returns success when invoking use case then returns same success")
    fun givenRepositoryReturnsSuccess_whenInvokingUseCase_thenReturnsSameSuccess() = runTest {
        // Given
        val expectedTemperature = 25
        val successResult = Result.Success(expectedTemperature)
        whenever(repository.getTodayTemperature()).thenReturn(successResult)

        // When
        val result = useCase.invoke()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(expectedTemperature, (result as Result.Success).data)
    }

    @Test
    @DisplayName("Given repository returns error when invoking use case then returns same error")
    fun givenRepositoryReturnsError_whenInvokingUseCase_thenReturnsSameError() = runTest {
        // Given
        val expectedMessage = "Network error"
        val errorResult = Result.Error(expectedMessage)
        whenever(repository.getTodayTemperature()).thenReturn(errorResult)

        // When
        val result = useCase.invoke()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(expectedMessage, (result as Result.Error).message)
    }

    @Test
    @DisplayName("Given repository returns loading when invoking use case then returns same loading")
    fun givenRepositoryReturnsLoading_whenInvokingUseCase_thenReturnsSameLoading() = runTest {
        // Given
        val loadingResult = Result.Loading
        whenever(repository.getTodayTemperature()).thenReturn(loadingResult)

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
        val expectedException = RuntimeException("Repository failed")
        whenever(repository.getTodayTemperature()).thenThrow(expectedException)

        // When & Then
        try {
            useCase.invoke()
            throw AssertionError("Expected exception was not thrown")
        } catch (e: RuntimeException) {
            assertEquals(expectedException.message, e.message)
        }
    }
}