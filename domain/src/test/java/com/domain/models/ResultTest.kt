package com.domain.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Result Model Tests")
class ResultTest {

    @Test
    @DisplayName("Given success data when creating Success result then contains data")
    fun givenSuccessData_whenCreatingSuccessResult_thenContainsData() {
        // Given
        val expectedData = "test data"

        // When
        val result = Result.Success(expectedData)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(expectedData, result.data)
    }

    @Test
    @DisplayName("Given error message when creating Error result then contains message")
    fun givenErrorMessage_whenCreatingErrorResult_thenContainsMessage() {
        // Given
        val expectedMessage = "Something went wrong"

        // When
        val result = Result.Error(expectedMessage)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(expectedMessage, result.message)
    }

    @Test
    @DisplayName("Given loading state when creating Loading result then is singleton")
    fun givenLoadingState_whenCreatingLoadingResult_thenIsSingleton() {
        // Given & When
        val loading1 = Result.Loading
        val loading2 = Result.Loading

        // Then
        assertTrue(loading1 is Result.Loading)
        assertTrue(loading2 is Result.Loading)
        assertEquals(loading1, loading2)
    }

    @Test
    @DisplayName("Given different result types when comparing then are different")
    fun givenDifferentResultTypes_whenComparing_thenAreDifferent() {
        // Given
        val success = Result.Success("data")
        val error = Result.Error("error")
        val loading = Result.Loading

        // When & Then
        assertTrue(success != error)
        assertTrue(success != loading)
        assertTrue(error != loading)
    }
}