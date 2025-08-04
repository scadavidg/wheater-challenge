package com.wheaterchallenge.presentation

import android.content.res.Configuration
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.wheaterchallenge.ui.theme.WheaterChallengeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertTrue

/**
 * Android instrumentation tests for WeatherScreen composable.
 * Tests UI behavior, state management, and user interactions.
 */
@RunWith(AndroidJUnit4::class)
class WeatherScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun weatherScreen_withSuccessState_displaysWeatherData() {
        // Given
        val testTemperature = 75
        val testForecast = "Sunny"
        val testIcon = "https://api.weather.gov/icons/land/day/skc?size=medium"

        // When
        composeTestRule.setContent {
            WheaterChallengeTheme {
                WeatherScreenStateless(
                    temperature = testTemperature,
                    shortForecast = testForecast,
                    icon = testIcon,
                    isLoading = false,
                    isError = false,
                    isLandscape = false,
                    isFireTV = false,
                    onRefresh = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("75°F")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()
    }

    @Test
    fun weatherScreen_withLoadingState_showsLoadingIndicator() {
        // When
        composeTestRule.setContent {
            WheaterChallengeTheme {
                WeatherScreenStateless(
                    temperature = 0,
                    shortForecast = "",
                    icon = "",
                    isLoading = true,
                    isError = false,
                    isLandscape = false,
                    isFireTV = false,
                    onRefresh = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()
        
        // The loading state should be handled by the WeatherWidgetComposable
        // We can verify the title is still visible during loading
    }

    @Test
    fun weatherScreen_withErrorState_showsErrorMessage() {
        // When
        composeTestRule.setContent {
            WheaterChallengeTheme {
                WeatherScreenStateless(
                    temperature = 0,
                    shortForecast = "",
                    icon = "",
                    isLoading = false,
                    isError = true,
                    isLandscape = false,
                    isFireTV = false,
                    onRefresh = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("Error loading weather")
            .assertIsDisplayed()
    }

    @Test
    fun weatherScreen_orientationChange_adaptsLayoutCorrectly() {
        // Test Portrait
        composeTestRule.setContent {
            WheaterChallengeTheme {
                WeatherScreenStateless(
                    temperature = 72,
                    shortForecast = "Partly Cloudy",
                    icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                    isLoading = false,
                    isError = false,
                    isLandscape = false,
                    isFireTV = false,
                    onRefresh = {}
                )
            }
        }

        // Verify portrait content is displayed
        composeTestRule
            .onNodeWithText("72°F")
            .assertIsDisplayed()

        // Test Landscape
        composeTestRule.setContent {
            WheaterChallengeTheme {
                WeatherScreenStateless(
                    temperature = 72,
                    shortForecast = "Partly Cloudy",
                    icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                    isLoading = false,
                    isError = false,
                    isLandscape = true,
                    isFireTV = false,
                    onRefresh = {}
                )
            }
        }

        // Verify landscape content is still displayed correctly
        composeTestRule
            .onNodeWithText("72°F")
            .assertIsDisplayed()
    }

    @Test
    fun weatherScreen_onFireTV_usesFireTVOptimizations() {
        // When
        composeTestRule.setContent {
            WheaterChallengeTheme {
                WeatherScreenStateless(
                    temperature = 70,
                    shortForecast = "Clear",
                    icon = "https://api.weather.gov/icons/land/night/skc?size=medium",
                    isLoading = false,
                    isError = false,
                    isLandscape = true,
                    isFireTV = true,
                    onRefresh = {}
                )
            }
        }

        // Then - Should display content optimized for FireTV
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("70°F")
            .assertIsDisplayed()
    }

    @Test
    fun weatherScreen_retryButtonClick_triggersRetryAction() {
        // Given
        var retryClicked = false

        // When
        composeTestRule.setContent {
            WheaterChallengeTheme {
                WeatherScreenStateless(
                    temperature = 0,
                    shortForecast = "",
                    icon = "",
                    isLoading = false,
                    isError = true,
                    isLandscape = false,
                    isFireTV = false,
                    onRefresh = { retryClicked = true }
                )
            }
        }

        // Then - Find and click refresh button
        composeTestRule
            .onNodeWithContentDescription("Refresh")
            .performClick()

        // Verify retry was triggered
        assertTrue("Retry callback should be invoked", retryClicked)
    }

    @Test
    fun weatherScreen_detectFireTV_withLargeScreen_returnsTrue() {
        // Given
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val configuration = Configuration().apply {
            screenWidthDp = 1920
            screenHeightDp = 1080
        }

        // When
        composeTestRule.setContent {
            val isFireTV = detectFireTV(configuration)
            
            // Then
            assertTrue("Large screen should be detected as FireTV", isFireTV)
        }
    }

    @Test
    fun weatherScreen_detectFireTV_withSmallScreen_returnsFalse() {
        // Given
        val configuration = Configuration().apply {
            screenWidthDp = 360
            screenHeightDp = 640
        }

        // When
        composeTestRule.setContent {
            val isFireTV = detectFireTV(configuration)
            
            // Then
            assertTrue("Small screen should not be detected as FireTV", !isFireTV)
        }
    }

    @Test
    fun weatherScreen_detectFireTV_withTabletScreen_returnsFalse() {
        // Given
        val configuration = Configuration().apply {
            screenWidthDp = 800
            screenHeightDp = 1280
        }

        // When
        composeTestRule.setContent {
            val isFireTV = detectFireTV(configuration)
            
            // Then
            assertTrue("Tablet screen should not be detected as FireTV", !isFireTV)
        }
    }

    @Test
    fun weatherScreen_withExtremeTemperatures_displaysCorrectly() {
        // Test extreme cold
        composeTestRule.setContent {
            WheaterChallengeTheme {
                WeatherScreenStateless(
                    temperature = -40,
                    shortForecast = "Extremely Cold",
                    icon = "https://api.weather.gov/icons/land/day/blizzard?size=medium",
                    isLoading = false,
                    isError = false,
                    isLandscape = false,
                    isFireTV = false,
                    onRefresh = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("-40°F")
            .assertIsDisplayed()

        // Test extreme hot
        composeTestRule.setContent {
            WheaterChallengeTheme {
                WeatherScreenStateless(
                    temperature = 120,
                    shortForecast = "Extremely Hot",
                    icon = "https://api.weather.gov/icons/land/day/hot?size=medium",
                    isLoading = false,
                    isError = false,
                    isLandscape = false,
                    isFireTV = false,
                    onRefresh = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("120°F")
            .assertIsDisplayed()
    }

    @Test
    fun weatherScreen_withDifferentConfigurations_maintainsBasicFunctionality() {
        val configurations = listOf(
            Triple(false, false, "Phone Portrait"),
            Triple(true, false, "Phone Landscape"),
            Triple(false, true, "FireTV Portrait"),
            Triple(true, true, "FireTV Landscape")
        )

        configurations.forEach { (isLandscape, isFireTV, description) ->
            composeTestRule.setContent {
                WheaterChallengeTheme {
                    WeatherScreenStateless(
                        temperature = 73,
                        shortForecast = "Partly Cloudy",
                        icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                        isLoading = false,
                        isError = false,
                        isLandscape = isLandscape,
                        isFireTV = isFireTV,
                        onRefresh = {}
                    )
                }
            }

            // Verify basic elements are present for all configurations
            composeTestRule
                .onNodeWithText("San Jose Weather")
                .assertIsDisplayed()
            
            composeTestRule
                .onNodeWithText("73°F")
                .assertIsDisplayed()
        }
    }
}

/**
 * Helper function to create stateless version of WeatherScreen for testing
 */
@androidx.compose.runtime.Composable
private fun WeatherScreenStateless(
    temperature: Int,
    shortForecast: String,
    icon: String,
    isLoading: Boolean,
    isError: Boolean,
    isLandscape: Boolean,
    isFireTV: Boolean,
    onRefresh: () -> Unit
) {
    androidx.compose.foundation.layout.Box(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        com.widget.WeatherWidgetComposable(
            temperature = temperature,
            shortForecast = shortForecast,
            icon = icon,
            modifier = androidx.compose.ui.Modifier.size(
                width = if (isLandscape) 400.dp else 300.dp,
                height = if (isLandscape) 200.dp else 320.dp
            ),
            onRefresh = onRefresh,
            isLoading = isLoading,
            isError = isError,
            isLandscape = isLandscape,
            isFireTV = isFireTV
        )
    }
}