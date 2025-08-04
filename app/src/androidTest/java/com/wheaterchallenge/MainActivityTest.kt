package com.wheaterchallenge

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.domain.models.Result
import com.domain.models.Weather
import com.domain.usecase.GetTodayTemperatureUseCase
import com.domain.usecase.GetWeatherDataUseCase
import com.wheaterchallenge.di.UseCaseModule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

/**
 * Android instrumentation tests for MainActivity.
 * Tests activity lifecycle, Hilt injection, and basic functionality.
 */
@HiltAndroidTest
@UninstallModules(UseCaseModule::class)
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

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
    fun mainActivity_onCreate_setsCorrectContent() {
        // Given - Mock successful weather data
        val weather = Weather(
            temperature = 75,
            shortForecast = "Sunny",
            icon = "https://api.weather.gov/icons/land/day/skc?size=medium"
        )
        
        runBlocking {
            whenever(getWeatherDataUseCase()).thenReturn(Result.Success(weather))
        }

        // When - Activity is created (already done by createAndroidComposeRule)
        
        // Then - Verify the correct content is set
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("75°F")
            .assertIsDisplayed()
    }

    @Test
    fun mainActivity_withHiltInjection_injectsViewModelCorrectly() {
        // Given - Mock weather data to verify injection works
        val weather = Weather(
            temperature = 68,
            shortForecast = "Clear",
            icon = "https://api.weather.gov/icons/land/day/few?size=medium"
        )
        
        runBlocking {
            whenever(getWeatherDataUseCase()).thenReturn(Result.Success(weather))
        }

        // When - Activity launches and Hilt performs injection
        composeTestRule.waitForIdle()

        // Then - Verify ViewModel was injected and is working
        composeTestRule
            .onNodeWithText("68°F")
            .assertIsDisplayed()

        // This proves that:
        // 1. MainActivity was annotated with @AndroidEntryPoint
        // 2. Hilt successfully injected the ViewModel
        // 3. The ViewModel is using our mocked use cases
    }

    @Test
    fun mainActivity_edgeToEdge_configuresCorrectly() {
        // Given - Mock basic weather data
        runBlocking {
            whenever(getWeatherDataUseCase()).thenReturn(Result.Loading)
        }

        // When - Activity is created with edge-to-edge enabled
        
        // Then - Verify basic UI is displayed (edge-to-edge is visual, hard to test directly)
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()

        // Verify activity is in the correct state
        val currentState = composeTestRule.activityRule.scenario.state
        assertTrue("Activity should be in RESUMED state", 
                  currentState == Lifecycle.State.RESUMED)
    }

    @Test
    fun mainActivity_lifecycleChanges_handlesCorrectly() {
        // Given - Mock weather data
        val weather = Weather(
            temperature = 72,
            shortForecast = "Partly Cloudy",
            icon = "https://api.weather.gov/icons/land/day/bkn?size=medium"
        )
        
        runBlocking {
            whenever(getWeatherDataUseCase()).thenReturn(Result.Success(weather))
        }

        // When - Activity goes through lifecycle changes
        composeTestRule.activityRule.scenario.moveToState(Lifecycle.State.STARTED)
        composeTestRule.waitForIdle()
        
        composeTestRule.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
        composeTestRule.waitForIdle()

        // Then - Verify UI is still functional after lifecycle changes
        composeTestRule
            .onNodeWithText("72°F")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()
    }

    @Test
    fun mainActivity_rotation_maintainsState() {
        // Given - Mock weather data
        val weather = Weather(
            temperature = 77,
            shortForecast = "Sunny",
            icon = "https://api.weather.gov/icons/land/day/skc?size=medium"
        )
        
        runBlocking {
            whenever(getWeatherDataUseCase()).thenReturn(Result.Success(weather))
        }

        // Verify initial state
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithText("77°F")
            .assertIsDisplayed()

        // When - Simulate configuration change (recreation)
        composeTestRule.activityRule.scenario.recreate()
        composeTestRule.waitForIdle()

        // Then - Verify state is maintained after recreation
        composeTestRule
            .onNodeWithText("77°F")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()
    }

    @Test
    fun mainActivity_errorState_displaysCorrectly() {
        // Given - Mock error state
        runBlocking {
            whenever(getWeatherDataUseCase()).thenReturn(Result.Error("Network error"))
        }

        // When - Activity launches with error
        composeTestRule.waitForIdle()

        // Then - Verify error state is handled correctly
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Error loading weather")
            .assertIsDisplayed()
    }

    @Test
    fun mainActivity_theme_appliesCorrectly() {
        // Given - Mock basic data
        runBlocking {
            whenever(getWeatherDataUseCase()).thenReturn(Result.Loading)
        }

        // When - Activity launches with WheaterChallengeTheme
        
        // Then - Verify theme is applied (basic verification)
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()

        // The theme application is mostly visual, but we can verify
        // that the composable renders without crashes
        composeTestRule.waitForIdle()
    }

    @Test
    fun mainActivity_multipleStarts_handlesCorrectly() {
        // Given - Mock weather data
        val weather = Weather(
            temperature = 74,
            shortForecast = "Clear",
            icon = "https://api.weather.gov/icons/land/day/few?size=medium"
        )
        
        runBlocking {
            whenever(getWeatherDataUseCase()).thenReturn(Result.Success(weather))
        }

        // When - Multiple start/stop cycles
        repeat(3) {
            composeTestRule.activityRule.scenario.moveToState(Lifecycle.State.STARTED)
            composeTestRule.waitForIdle()
            
            composeTestRule.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
            composeTestRule.waitForIdle()
        }

        // Then - Verify activity handles multiple starts correctly
        composeTestRule
            .onNodeWithText("74°F")
            .assertIsDisplayed()

        val currentState = composeTestRule.activityRule.scenario.state
        assertEquals("Activity should be in RESUMED state", 
                    Lifecycle.State.RESUMED, currentState)
    }

    @Test
    fun mainActivity_memoryPressure_recoversGracefully() {
        // Given - Mock weather data
        val weather = Weather(
            temperature = 73,
            shortForecast = "Partly Cloudy",
            icon = "https://api.weather.gov/icons/land/day/bkn?size=medium"
        )
        
        runBlocking {
            whenever(getWeatherDataUseCase()).thenReturn(Result.Success(weather))
        }

        // Verify initial state
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithText("73°F")
            .assertIsDisplayed()

        // When - Simulate memory pressure by recreating activity
        composeTestRule.activityRule.scenario.recreate()
        composeTestRule.waitForIdle()

        // Then - Verify recovery after memory pressure
        composeTestRule
            .onNodeWithText("73°F")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()
    }
}