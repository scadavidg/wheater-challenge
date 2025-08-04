package com.widget
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Basic instrumented tests for WeatherWidget using simplified approach.
 * 
 * These tests validate core UI functionality that can be tested with
 * the basic Compose testing framework.
 */
@RunWith(AndroidJUnit4::class)
class WeatherWidgetTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun weatherWidget_displaysTemperature() {
        // Given
        val temperature = 72

        // When
        composeTestRule.setContent {
            WeatherWidgetComposable(
                temperature = temperature,
                shortForecast = "Sunny",
                icon = "https://api.weather.gov/icons/land/day/skc?size=medium",
                onRefresh = { }
            )
        }

        // Then
        composeTestRule
            .onNodeWithText("72°F")
            .assertIsDisplayed()
    }

    @Test
    fun weatherWidget_displaysTitle() {
        // When
        composeTestRule.setContent {
            WeatherWidgetComposable(
                temperature = 75,
                shortForecast = "Clear",
                icon = "https://api.weather.gov/icons/land/day/few?size=medium",
                onRefresh = { }
            )
        }

        // Then
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()
    }

    @Test
    fun weatherWidget_showsErrorMessage() {
        // When
        composeTestRule.setContent {
            WeatherWidgetComposable(
                temperature = 72,
                shortForecast = "Partly Cloudy",
                icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                onRefresh = { },
                isError = true
            )
        }

        // Then
        composeTestRule
            .onNodeWithText("Error loading weather")
            .assertIsDisplayed()
    }

    @Test
    fun weatherWidget_refreshButtonInteraction() {
        // Given
        var refreshClicked = false

        // When
        composeTestRule.setContent {
            WeatherWidgetComposable(
                temperature = 78,
                onRefresh = { refreshClicked = true },
                shortForecast = "Partly Cloudy",
                icon = "https://api.weather.gov/icons/land/day/bkn?size=medium"
            )
        }

        // Then - find and click refresh button by content description
        composeTestRule
            .onNodeWithContentDescription("Refresh")
            .performClick()

        // Verify callback was invoked
        assert(refreshClicked) { "Refresh callback should be invoked" }
    }

    @Test
    fun weatherWidget_extremeColdTemperature() {
        // Test very cold temperature
        composeTestRule.setContent {
            WeatherWidgetComposable(
                temperature = -40,
                shortForecast = "Partly Cloudy",
                icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                onRefresh = { }
            )
        }

        composeTestRule
            .onNodeWithText("-40°F")
            .assertIsDisplayed()
    }

    @Test
    fun weatherWidget_extremeHotTemperature() {
        // Test very hot temperature
        composeTestRule.setContent {
            WeatherWidgetComposable(
                temperature = 110,
                shortForecast = "Partly Cloudy",
                icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                onRefresh = { }
            )
        }

        composeTestRule
            .onNodeWithText("110°F")
            .assertIsDisplayed()
    }

    @Test
    fun weatherWidget_landscapeMode() {
        // When
        composeTestRule.setContent {
            WeatherWidgetComposable(
                temperature = 80,
                shortForecast = "Partly Cloudy",
                icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                onRefresh = { },
                isLandscape = true
            )
        }

        // Then - should still display all basic elements
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("80°F")
            .assertIsDisplayed()
    }

    @Test
    fun weatherWidget_fireTVMode() {
        // When
        composeTestRule.setContent {
            WeatherWidgetComposable(
                temperature = 75,
                shortForecast = "Partly Cloudy",
                icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                onRefresh = { },
                isFireTV = true
            )
        }

        // Then - should render with FireTV optimizations
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("75°F")
            .assertIsDisplayed()
    }

    @Test
    fun weatherWidget_normalStateDisplaysContent() {
        composeTestRule.setContent {
            WeatherWidgetComposable(
                temperature = 73,
                shortForecast = "Partly Cloudy",
                icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                onRefresh = { },
                isLoading = false,
                isError = false
            )
        }

        // Title should be visible
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()

        // Temperature should be displayed
        composeTestRule
            .onNodeWithText("73°F")
            .assertIsDisplayed()
    }

    @Test
    fun weatherWidget_errorStateDisplaysContent() {
        composeTestRule.setContent {
            WeatherWidgetComposable(
                temperature = 73,
                shortForecast = "Partly Cloudy",
                icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                onRefresh = { },
                isLoading = false,
                isError = true
            )
        }

        // Title should be visible
        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()

        // Error message should be displayed
        composeTestRule
            .onNodeWithText("Error loading weather")
            .assertIsDisplayed()
    }

    @Test
    fun weatherWidget_androidPortraitConfiguration() {
        composeTestRule.setContent {
            WeatherWidgetComposable(
                temperature = 77,
                shortForecast = "Partly Cloudy",
                icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                onRefresh = { },
                isLandscape = false,
                isFireTV = false
            )
        }

        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("77°F")
            .assertIsDisplayed()
    }

    @Test
    fun weatherWidget_androidLandscapeConfiguration() {
        composeTestRule.setContent {
            WeatherWidgetComposable(
                temperature = 77,
                shortForecast = "Partly Cloudy",
                icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                onRefresh = { },
                isLandscape = true,
                isFireTV = false
            )
        }

        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("77°F")
            .assertIsDisplayed()
    }

    @Test
    fun weatherWidget_fireTVPortraitConfiguration() {
        composeTestRule.setContent {
            WeatherWidgetComposable(
                temperature = 77,
                shortForecast = "Partly Cloudy",
                icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                onRefresh = { },
                isLandscape = false,
                isFireTV = true
            )
        }

        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("77°F")
            .assertIsDisplayed()
    }

    @Test
    fun weatherWidget_fireTVLandscapeConfiguration() {
        composeTestRule.setContent {
            WeatherWidgetComposable(
                temperature = 77,
                shortForecast = "Partly Cloudy",
                icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                onRefresh = { },
                isLandscape = true,
                isFireTV = true
            )
        }

        composeTestRule
            .onNodeWithText("San Jose Weather")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("77°F")
            .assertIsDisplayed()
    }

    @Test
    fun weatherWidget_zeroTemperature() {
        // When
        composeTestRule.setContent {
            WeatherWidgetComposable(
                temperature = 0,
                shortForecast = "Partly Cloudy",
                icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                onRefresh = { }
            )
        }

        // Then
        composeTestRule
            .onNodeWithText("0°F")
            .assertIsDisplayed()
    }
}