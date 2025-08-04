package com.wheaterchallenge.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wheaterchallenge.ui.theme.WheaterChallengeTheme
import com.widget.WeatherWidgetComposable

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    
    // Detect orientation
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    
    // Detect platform - check for FireTV characteristics
    val isFireTV = detectFireTV(configuration)
    
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            WeatherWidgetComposable(
                temperature = uiState.temperature,
                shortForecast = uiState.shortForecast,
                icon = uiState.icon,
                modifier = Modifier.size(
                    width = if (isLandscape) 400.dp else 300.dp,
                    height = if (isLandscape) 200.dp else 320.dp
                ),
                onRefresh = { viewModel.retry() },
                isLoading = uiState.isLoading,
                isError = uiState.isError,
                isLandscape = isLandscape,
                isFireTV = isFireTV
            )
        }
    }
}

@Composable
fun detectFireTV(configuration: Configuration): Boolean {
    // FireTV detection logic - typically larger screens and different screen characteristics
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp
    
    // FireTV typically has larger screens (typically 720p+ and designed for TV viewing)
    return screenWidthDp >= 960 || screenHeightDp >= 540
}

// ===== PREVIEWS =====

@Preview(
    name = "Weather Screen - Phone Portrait",
    showBackground = true,
    device = Devices.PIXEL_4
)
@Composable
fun WeatherScreenPhonePortraitPreview() {
    WheaterChallengeTheme {
        WeatherScreenStateless(
            temperature = 72,
            shortForecast = "Sunny",
            icon = "https://api.weather.gov/icons/land/day/skc?size=medium",
            isLoading = false,
            isError = false,
            isLandscape = false,
            isFireTV = false,
            onRefresh = {}
        )
    }
}

@Preview(
    name = "Weather Screen - Phone Landscape", 
    showBackground = true,
    device = Devices.PIXEL_4,
    widthDp = 891,
    heightDp = 411
)
@Composable
fun WeatherScreenPhoneLandscapePreview() {
    WheaterChallengeTheme {
        WeatherScreenStateless(
            temperature = 68,
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

@Preview(
    name = "Weather Screen - Tablet Portrait",
    showBackground = true,
    device = Devices.PIXEL_C
)
@Composable
fun WeatherScreenTabletPortraitPreview() {
    WheaterChallengeTheme {
        WeatherScreenStateless(
            temperature = 75,
            shortForecast = "Clear",
            icon = "https://api.weather.gov/icons/land/day/few?size=medium",
            isLoading = false,
            isError = false,
            isLandscape = false,
            isFireTV = false,
            onRefresh = {}
        )
    }
}

@Preview(
    name = "Weather Screen - FireTV",
    showBackground = true,
    widthDp = 1920,
    heightDp = 1080
)
@Composable
fun WeatherScreenFireTVPreview() {
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

@Preview(
    name = "Weather Screen - Loading State",
    showBackground = true,
    device = Devices.PIXEL_4
)
@Composable
fun WeatherScreenLoadingPreview() {
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

@Preview(
    name = "Weather Screen - Error State",
    showBackground = true,
    device = Devices.PIXEL_4
)
@Composable
fun WeatherScreenErrorPreview() {
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

/**
 * Stateless version of WeatherScreen for previews
 */
@Composable
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
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            WeatherWidgetComposable(
                temperature = temperature,
                shortForecast = shortForecast,
                icon = icon,
                modifier = Modifier.size(
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
}