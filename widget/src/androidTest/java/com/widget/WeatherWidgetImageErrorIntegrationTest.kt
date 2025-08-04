package com.widget

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherWidgetImageErrorIntegrationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun weatherWidget_withEmptyIconUrl_showsFallbackIcon() {
        // Given
        composeTestRule.setContent {
            MaterialTheme {
                WeatherWidgetComposable(
                    temperature = 75,
                    shortForecast = "Sunny",
                    icon = "", // Empty icon URL
                    onRefresh = { },
                    isLoading = false,
                    isError = false,
                    isLandscape = false,
                    isFireTV = false
                )
            }
        }

        // Then - Should show fallback icon
        composeTestRule
            .onNodeWithContentDescription("Weather icon (fallback)")
            .assertIsDisplayed()
    }

    @Test
    fun weatherWidget_withValidIconUrl_attemptsToShowAsyncImage() {
        // Given
        composeTestRule.setContent {
            MaterialTheme {
                WeatherWidgetComposable(
                    temperature = 72,
                    shortForecast = "Partly Cloudy",
                    icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                    onRefresh = { },
                    isLoading = false,
                    isError = false,
                    isLandscape = false,
                    isFireTV = false
                )
            }
        }

        // Then - AsyncImage should be present (though actual loading depends on network)
        // We can't easily test the AsyncImage loading success/failure in unit tests,
        // but we can verify the component structure is correct
        composeTestRule
            .onNodeWithContentDescription("Weather icon for Partly Cloudy")
            .assertIsDisplayed()
    }

    @Test
    fun weatherWidget_withInvalidIconUrl_attemptsToShowAsyncImageButHandlesErrorGracefully() {
        // Given
        composeTestRule.setContent {
            MaterialTheme {
                WeatherWidgetComposable(
                    temperature = 68,
                    shortForecast = "Hot",
                    icon = "https://invalid-domain.fake/nonexistent-icon.png",
                    onRefresh = { },
                    isLoading = false,
                    isError = false,
                    isLandscape = false,
                    isFireTV = false
                )
            }
        }

        // Then - AsyncImage should be present (Coil will handle the error internally)
        composeTestRule
            .onNodeWithContentDescription("Weather icon for Hot")
            .assertIsDisplayed()
    }

    @Test
    fun weatherWidget_inLoadingState_doesNotShowAnyIcon() {
        // Given
        composeTestRule.setContent {
            MaterialTheme {
                WeatherWidgetComposable(
                    temperature = 0,
                    shortForecast = "",
                    icon = "https://api.weather.gov/icons/land/day/skc?size=medium",
                    onRefresh = { },
                    isLoading = true,
                    isError = false,
                    isLandscape = false,
                    isFireTV = false
                )
            }
        }

        // Then - No weather icons should be displayed in loading state
        composeTestRule
            .onNodeWithContentDescription("Weather icon for ")
            .assertDoesNotExist()
        
        composeTestRule
            .onNodeWithContentDescription("Weather icon (fallback)")
            .assertDoesNotExist()
    }

    @Test
    fun weatherWidget_inErrorState_doesNotShowAnyIcon() {
        // Given
        composeTestRule.setContent {
            MaterialTheme {
                WeatherWidgetComposable(
                    temperature = 0,
                    shortForecast = "",
                    icon = "https://api.weather.gov/icons/land/day/skc?size=medium",
                    onRefresh = { },
                    isLoading = false,
                    isError = true,
                    isLandscape = false,
                    isFireTV = false
                )
            }
        }

        // Then - No weather icons should be displayed in error state
        composeTestRule
            .onNodeWithContentDescription("Weather icon for ")
            .assertDoesNotExist()
        
        composeTestRule
            .onNodeWithContentDescription("Weather icon (fallback)")
            .assertDoesNotExist()
    }

    @Test
    fun weatherWidget_landscapeWithEmptyIcon_showsFallbackIcon() {
        // Given
        composeTestRule.setContent {
            MaterialTheme {
                WeatherWidgetComposable(
                    temperature = 70,
                    shortForecast = "Clear",
                    icon = "", // Empty icon URL in landscape
                    onRefresh = { },
                    isLoading = false,
                    isError = false,
                    isLandscape = true,
                    isFireTV = false
                )
            }
        }

        // Then - Should show fallback icon in landscape layout
        composeTestRule
            .onNodeWithContentDescription("Weather icon (fallback)")
            .assertIsDisplayed()
    }

    @Test
    fun weatherWidget_fireTVWithEmptyIcon_showsFallbackIcon() {
        // Given
        composeTestRule.setContent {
            MaterialTheme {
                WeatherWidgetComposable(
                    temperature = 78,
                    shortForecast = "Sunny",
                    icon = "", // Empty icon URL on FireTV
                    onRefresh = { },
                    isLoading = false,
                    isError = false,
                    isLandscape = true,
                    isFireTV = true
                )
            }
        }

        // Then - Should show fallback icon optimized for FireTV
        composeTestRule
            .onNodeWithContentDescription("Weather icon (fallback)")
            .assertIsDisplayed()
    }
}