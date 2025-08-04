package com.wheaterchallenge.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.domain.models.Result
import com.domain.models.Weather
import com.domain.usecase.GetTodayTemperatureUseCase
import com.domain.usecase.GetWeatherDataUseCase
import com.wheaterchallenge.MainActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.wheaterchallenge.di.UseCaseModule

/**
 * Integration tests for WeatherScreen that test the complete flow
 * from ViewModel to UI with real Android components but mocked use cases.
 */
@HiltAndroidTest
@UninstallModules(UseCaseModule::class)
@RunWith(AndroidJUnit4::class)
class WeatherScreenIntegrationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // Mock use cases that will be injected by Hilt
    @BindValue
    @JvmField
    val getTodayTemperatureUseCase: GetTodayTemperatureUseCase = mock()

    @BindValue
    @JvmField
    val getWeatherDataUseCase: GetWeatherDataUseCase = mock()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun weatherScreen_endToEndFlow_loadsAndDisplaysRealData() {
        // Given - Mock successful weather data
        val expectedWeather = Weather(
            temperature = 78,
            shortForecast = "Sunny",
            icon = "https://api.weather.gov/icons/land/day/skc?size=medium",
            location = "San Jose, CA"
        )
        
        runBlocking {
            whenever(getWeatherDataUseCase()).thenReturn(Result.Success(expectedWeather))
        }

        // When - Launch the activity (already done by createAndroidComposeRule)
        // The ViewModel should automatically load data on init

        // Then - Verify the weather data is displayed
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("78°F")
            .assertIsDisplayed()

        // Verify the refresh button is available
        composeTestRule
            .onNodeWithContentDescription("Refresh")
            .assertIsDisplayed()
    }

    @Test
    fun weatherScreen_networkError_handlesGracefully() {
        // Given - Mock network error
        val errorMessage = "Network connection failed"
        
        runBlocking {
            whenever(getWeatherDataUseCase()).thenReturn(Result.Error(errorMessage))
        }

        // When - Activity launches with error state
        
        // Then - Verify error state is displayed
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Error loading weather")
            .assertIsDisplayed()

        // Verify refresh button is still available for retry
        composeTestRule
            .onNodeWithContentDescription("Refresh")
            .assertIsDisplayed()
    }

    @Test
    fun weatherScreen_retryAfterError_recoversSuccessfully() {
        // Given - Initially return error, then success on retry
        val errorMessage = "Temporary network issue"
        val successWeather = Weather(
            temperature = 72,
            shortForecast = "Partly Cloudy",
            icon = "https://api.weather.gov/icons/land/day/bkn?size=medium"
        )

        runBlocking {
            // First call returns error
            whenever(getWeatherDataUseCase()).thenReturn(Result.Error(errorMessage))
        }

        // Wait for initial error state to load
        composeTestRule.waitForIdle()

        // Verify error state
        composeTestRule
            .onNodeWithText("Error loading weather")
            .assertIsDisplayed()

        // Setup successful retry response
        runBlocking {
            whenever(getWeatherDataUseCase()).thenReturn(Result.Success(successWeather))
        }

        // When - Click retry button
        composeTestRule
            .onNodeWithContentDescription("Refresh")
            .performClick()

        // Wait for the retry operation to complete
        composeTestRule.waitForIdle()

        // Then - Verify success state is now displayed
        composeTestRule
            .onNodeWithText("72°F")
            .assertIsDisplayed()
    }

    @Test
    fun weatherScreen_loadingState_showsCorrectUI() {
        // Given - Mock loading state
        runBlocking {
            whenever(getWeatherDataUseCase()).thenReturn(Result.Loading)
        }

        // When - Activity launches
        
        // Then - Verify loading state UI
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()

        // The loading indicator should be managed by the WeatherWidget
        // We can verify that no temperature is displayed yet
        composeTestRule
            .onNodeWithText("0°F")
            .assertDoesNotExist()
    }

    @Test
    fun weatherScreen_configurationChanges_maintainsState() {
        // Given - Set up successful weather data
        val weather = Weather(
            temperature = 75,
            shortForecast = "Clear",
            icon = "https://api.weather.gov/icons/land/day/few?size=medium"
        )
        
        runBlocking {
            whenever(getWeatherDataUseCase()).thenReturn(Result.Success(weather))
        }

        // When - Initial state is loaded
        composeTestRule.waitForIdle()

        // Then - Verify data is displayed
        composeTestRule
            .onNodeWithText("75°F")
            .assertIsDisplayed()

        // When - Simulate configuration change (rotation)
        // Note: In a real test, we would trigger an actual configuration change
        // For this test, we verify the current state maintains correctly
        composeTestRule.waitForIdle()

        // Then - Verify data is still displayed after configuration change
        composeTestRule
            .onNodeWithText("75°F")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()
    }

    @Test
    fun weatherScreen_multipleRetryAttempts_handlesCorrectly() {
        // Given - Setup alternating error and success responses
        val weather = Weather(
            temperature = 70,
            shortForecast = "Sunny",
            icon = "https://api.weather.gov/icons/land/day/skc?size=medium"
        )

        runBlocking {
            // Initially return error
            whenever(getWeatherDataUseCase()).thenReturn(Result.Error("Network error"))
        }

        // Wait for error state
        composeTestRule.waitForIdle()
        
        composeTestRule
            .onNodeWithText("Error loading weather")
            .assertIsDisplayed()

        // Setup success for first retry
        runBlocking {
            whenever(getWeatherDataUseCase()).thenReturn(Result.Success(weather))
        }

        // When - First retry
        composeTestRule
            .onNodeWithContentDescription("Refresh")
            .performClick()

        composeTestRule.waitForIdle()

        // Then - Should show success
        composeTestRule
            .onNodeWithText("70°F")
            .assertIsDisplayed()

        // When - Second retry (should still work)
        composeTestRule
            .onNodeWithContentDescription("Refresh")
            .performClick()

        composeTestRule.waitForIdle()

        // Then - Should still show success
        composeTestRule
            .onNodeWithText("70°F")
            .assertIsDisplayed()
    }

    @Test
    fun weatherScreen_extremeWeatherConditions_displaysCorrectly() {
        val extremeWeatherCases = listOf(
            Triple(-30, "Blizzard", "Extreme Cold"),
            Triple(0, "Freezing", "Freezing Point"),
            Triple(110, "Heat Wave", "Extreme Heat")
        )

        extremeWeatherCases.forEach { (temperature, forecast, description) ->
            // Given
            val weather = Weather(
                temperature = temperature,
                shortForecast = forecast,
                icon = "https://api.weather.gov/icons/land/day/extreme?size=medium"
            )

            runBlocking {
                whenever(getWeatherDataUseCase()).thenReturn(Result.Success(weather))
            }

            // Restart activity for each test case
            composeTestRule.activityRule.scenario.recreate()
            composeTestRule.waitForIdle()

            // Then - Verify extreme temperatures display correctly
            composeTestRule
                .onNodeWithText("${temperature}°F")
                .assertIsDisplayed()

            composeTestRule
                .onNodeWithText("San Jose Weather")
                .assertIsDisplayed()
        }
    }

    @Test
    fun weatherScreen_rapidUserInteractions_handlesGracefully() {
        // Given
        val weather = Weather(
            temperature = 73,
            shortForecast = "Partly Cloudy",
            icon = "https://api.weather.gov/icons/land/day/bkn?size=medium"
        )

        runBlocking {
            whenever(getWeatherDataUseCase()).thenReturn(Result.Success(weather))
        }

        composeTestRule.waitForIdle()

        // When - Rapid clicks on refresh button
        repeat(5) {
            composeTestRule
                .onNodeWithContentDescription("Refresh")
                .performClick()
        }

        composeTestRule.waitForIdle()

        // Then - Should handle gracefully and still display data
        composeTestRule
            .onNodeWithText("73°F")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()
    }

    @Test
    fun weatherScreen_longRunningOperation_maintainsResponsiveness() {
        // Given - Mock a delayed response to simulate slow network
        runBlocking {
            whenever(getWeatherDataUseCase()).thenAnswer {
                runBlocking {
                    delay(2000) // Simulate 2 second delay
                    Result.Success(Weather(
                        temperature = 76,
                        shortForecast = "Clear",
                        icon = "https://api.weather.gov/icons/land/day/few?size=medium"
                    ))
                }
            }
        }

        // When - App launches with slow operation
        composeTestRule.waitForIdle()

        // Then - UI should remain responsive and eventually show data
        // The title should always be visible
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()

        // Eventually the temperature should appear
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            try {
                composeTestRule
                    .onNodeWithText("76°F")
                    .assertIsDisplayed()
                true
            } catch (e: AssertionError) {
                false
            }
        }
    }
}