package com.widget.preview

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.widget.WeatherWidgetComposable

/**
 * Integration previews for the main WeatherWidgetComposable
 * 
 * Focus: States, themes, platforms, and edge cases testing
 * Layout-specific testing is done in individual composable files
 */
class WeatherWidgetComposablePreviews {

    // ===== WIDGET STATES =====

    @Preview(
        name = "Widget State - Loading",
        device = Devices.PIXEL_4,
        showBackground = true,
        backgroundColor = 0xFFF5F5F5
    )
    @Composable
    fun WeatherWidgetLoading() {
        MaterialTheme {
            Surface {
                WeatherWidgetComposable(
                    temperature = 0,
                    shortForecast = "",
                    icon = "",
                    onRefresh = { },
                    isLoading = true,
                    isLandscape = false,
                    isFireTV = false
                )
            }
        }
    }

    @Preview(
        name = "Widget State - Error",
        device = Devices.PIXEL_4,
        showBackground = true,
        backgroundColor = 0xFFF5F5F5
    )
    @Composable
    fun WeatherWidgetError() {
        MaterialTheme {
            Surface {
                WeatherWidgetComposable(
                    temperature = 0,
                    shortForecast = "",
                    icon = "",
                    onRefresh = { },
                    isError = true,
                    isLandscape = false,
                    isFireTV = false
                )
            }
        }
    }

    @Preview(
        name = "Widget State - Normal Portrait",
        device = Devices.PIXEL_4,
        showBackground = true,
        backgroundColor = 0xFFF5F5F5
    )
    @Composable
    fun WeatherWidgetNormalPortrait() {
        MaterialTheme {
            Surface {
                WeatherWidgetComposable(
                    temperature = 72,
                    shortForecast = "Sunny",
                    icon = "https://api.weather.gov/icons/land/day/skc?size=medium",
                    onRefresh = { },
                    isLandscape = false,
                    isFireTV = false
                )
            }
        }
    }

    @Preview(
        name = "Widget State - Normal Landscape",
        device = "spec:width=891dp,height=411dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape",
        showBackground = true,
        backgroundColor = 0xFFF5F5F5
    )
    @Composable
    fun WeatherWidgetNormalLandscape() {
        MaterialTheme {
            Surface {
                WeatherWidgetComposable(
                    temperature = 85,
                    shortForecast = "Hot",
                    icon = "https://api.weather.gov/icons/land/day/hot?size=medium",
                    onRefresh = { },
                    isLandscape = true,
                    isFireTV = false
                )
            }
        }
    }

    // ===== AMAZON FIRE TV PLATFORM =====

    @Preview(
        name = "Platform - FireTV Normal",
        device = "spec:width=1920dp,height=1080dp,dpi=320",
        showBackground = true,
        backgroundColor = 0xFF121212
    )
    @Composable
    fun WeatherWidgetFireTVNormal() {
        MaterialTheme(colorScheme = darkColorScheme()) {
            Surface {
                WeatherWidgetComposable(
                    temperature = 78,
                    shortForecast = "Clear",
                    icon = "https://api.weather.gov/icons/land/night/skc?size=medium",
                    onRefresh = { },
                    isLandscape = true, // FireTV typically uses landscape
                    isFireTV = true
                )
            }
        }
    }

    @Preview(
        name = "Platform - FireTV Loading",
        device = "spec:width=1920dp,height=1080dp,dpi=320",
        showBackground = true,
        backgroundColor = 0xFF121212
    )
    @Composable
    fun WeatherWidgetFireTVLoading() {
        MaterialTheme(colorScheme = darkColorScheme()) {
            Surface {
                WeatherWidgetComposable(
                    temperature = 0,
                    shortForecast = "",
                    icon = "",
                    onRefresh = { },
                    isLoading = true,
                    isLandscape = true,
                    isFireTV = true
                )
            }
        }
    }

    // ===== THEME TESTING =====

    @Preview(
        name = "Theme - Dark Mode",
        device = Devices.PIXEL_4,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true,
        backgroundColor = 0xFF121212
    )
    @Composable
    fun WeatherWidgetDarkTheme() {
        MaterialTheme(colorScheme = darkColorScheme()) {
            Surface {
                WeatherWidgetComposable(
                    temperature = 69,
                    shortForecast = "Partly Cloudy",
                    icon = "https://api.weather.gov/icons/land/night/bkn?size=medium",
                    onRefresh = { },
                    isLandscape = false,
                    isFireTV = false
                )
            }
        }
    }

    // ===== EDGE CASES =====

    @Preview(
        name = "Edge Case - High Temperature",
        device = Devices.PIXEL_4,
        showBackground = true,
        backgroundColor = 0xFFF5F5F5
    )
    @Composable
    fun WeatherWidgetHighTemp() {
        MaterialTheme {
            Surface {
                WeatherWidgetComposable(
                    temperature = 125,
                    shortForecast = "Extremely Hot",
                    icon = "https://api.weather.gov/icons/land/day/hot?size=medium",
                    onRefresh = { },
                    isLandscape = false,
                    isFireTV = false
                )
            }
        }
    }

    @Preview(
        name = "Edge Case - Low Temperature",
        device = Devices.PIXEL_4,
        showBackground = true,
        backgroundColor = 0xFFF5F5F5
    )
    @Composable
    fun WeatherWidgetLowTemp() {
        MaterialTheme {
            Surface {
                WeatherWidgetComposable(
                    temperature = -25,
                    shortForecast = "Snow",
                    icon = "https://api.weather.gov/icons/land/day/snow?size=medium",
                    onRefresh = { },
                    isLandscape = false,
                    isFireTV = false
                )
            }
        }
    }

    @Preview(
        name = "Edge Case - Zero Temperature",
        device = Devices.PIXEL_4,
        showBackground = true,
        backgroundColor = 0xFFF5F5F5
    )
    @Composable
    fun WeatherWidgetZeroTemp() {
        MaterialTheme {
            Surface {
                WeatherWidgetComposable(
                    temperature = 0,
                    shortForecast = "Freezing",
                    icon = "https://api.weather.gov/icons/land/day/blizzard?size=medium",
                    onRefresh = { },
                    isLandscape = false,
                    isFireTV = false
                )
            }
        }
    }

    // ===== IMAGE ERROR HANDLING PREVIEWS =====

    @Preview(
        name = "Image Error - Empty Icon URL",
        device = Devices.PIXEL_4,
        showBackground = true,
        backgroundColor = 0xFFF5F5F5
    )
    @Composable
    fun WeatherWidgetEmptyIconUrl() {
        MaterialTheme {
            Surface {
                WeatherWidgetComposable(
                    temperature = 75,
                    shortForecast = "Sunny",
                    icon = "", // Empty icon URL - should show fallback
                    onRefresh = { },
                    isLandscape = false,
                    isFireTV = false
                )
            }
        }
    }

    @Preview(
        name = "Image Error - Invalid Icon URL",
        device = Devices.PIXEL_4,
        showBackground = true,
        backgroundColor = 0xFFF5F5F5
    )
    @Composable
    fun WeatherWidgetInvalidIconUrl() {
        MaterialTheme {
            Surface {
                WeatherWidgetComposable(
                    temperature = 68,
                    shortForecast = "Partly Cloudy",
                    icon = "https://invalid-domain.fake/nonexistent-icon.png", // Invalid URL - will fail to load
                    onRefresh = { },
                    isLandscape = false,
                    isFireTV = false
                )
            }
        }
    }

    @Preview(
        name = "Image Error - 404 Icon URL",
        device = Devices.PIXEL_4,
        showBackground = true,
        backgroundColor = 0xFFF5F5F5
    )
    @Composable
    fun WeatherWidget404IconUrl() {
        MaterialTheme {
            Surface {
                WeatherWidgetComposable(
                    temperature = 82,
                    shortForecast = "Hot",
                    icon = "https://api.weather.gov/icons/land/day/nonexistent-404.png", // 404 URL
                    onRefresh = { },
                    isLandscape = false,
                    isFireTV = false
                )
            }
        }
    }

    @Preview(
        name = "Image Error - Landscape Empty Icon",
        device = "spec:width=891dp,height=411dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape",
        showBackground = true,
        backgroundColor = 0xFFF5F5F5
    )
    @Composable
    fun WeatherWidgetLandscapeEmptyIcon() {
        MaterialTheme {
            Surface {
                WeatherWidgetComposable(
                    temperature = 72,
                    shortForecast = "Clear",
                    icon = "", // Empty icon in landscape
                    onRefresh = { },
                    isLandscape = true,
                    isFireTV = false
                )
            }
        }
    }
}