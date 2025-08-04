package com.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * Portrait layout content for Weather Widget
 * 
 * Optimized for vertical space with centered content alignment.
 * Adapts sizing and spacing based on platform (Android vs FireTV).
 * Displays temperature, weather condition, and weather icon.
 */
@Composable
fun WeatherWidgetPortraitContentComposable(
    temperature: Int,
    shortForecast: String,
    icon: String,
    onRefresh: () -> Unit,
    isLoading: Boolean = false,
    isError: Boolean = false,
    isFireTV: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val padding = if (isFireTV) 24.dp else 16.dp
    val textSize = if (isFireTV) 
        MaterialTheme.typography.displayLarge 
    else MaterialTheme.typography.displayMedium
    
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Refresh button in top-right corner (like close button)
        IconButton(
            onClick = onRefresh,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = if (isFireTV) 4.dp else 2.dp, end = if (isFireTV) 4.dp else 2.dp)
                .size(if (isFireTV) 48.dp else 36.dp)
        ) {
            Icon(
                Icons.Default.Refresh, 
                contentDescription = "Refresh",
                modifier = Modifier.size(if (isFireTV) 28.dp else 18.dp)
            )
        }
        
        // Main content centered
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = padding,
                    end = padding,
                    top = padding + if (isFireTV) 12.dp else 6.dp, // Reduced top padding since button is smaller
                    bottom = padding
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "San Jose Weather",
                style = if (isFireTV) 
                    MaterialTheme.typography.headlineMedium 
                else MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(if (isFireTV) 16.dp else 12.dp))
            
            // Weather Icon with error handling
            if (!isLoading && !isError && icon.isNotEmpty()) {
                AsyncImage(
                    model = icon,
                    contentDescription = "Weather icon for $shortForecast",
                    modifier = Modifier.size(if (isFireTV) 80.dp else 64.dp),
                    onError = { 
                        // Log error for debugging (in real app, use proper logging)
                        println("Weather icon failed to load: ${it.result.throwable?.message}")
                    }
                )
            } else if (!isLoading && !isError && icon.isEmpty()) {
                // Show fallback icon when no icon URL provided
                Icon(
                    Icons.Default.Info,
                    contentDescription = "Weather icon (fallback)",
                    modifier = Modifier.size(if (isFireTV) 60.dp else 48.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            }
            
            // Consistent spacing after icon section
            if (!isLoading && !isError && (icon.isNotEmpty() || icon.isEmpty())) {
                Spacer(modifier = Modifier.height(if (isFireTV) 12.dp else 8.dp))
            }
            
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(if (isFireTV) 64.dp else 48.dp)
                    )
                }
                isError -> {
                    Text(
                        text = "Error loading weather",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
                else -> {
                    Text(
                        text = "${temperature}°F",
                        style = textSize,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(if (isFireTV) 8.dp else 4.dp))
            
            // Weather Description
            if (!isLoading && !isError && shortForecast.isNotEmpty()) {
                Text(
                    text = shortForecast,
                    style = if (isFireTV) 
                        MaterialTheme.typography.titleLarge 
                    else MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ===== LAYOUT-SPECIFIC PREVIEWS =====
// Focus: Portrait layout behavior across form factors

@Preview(
    name = "Portrait Layout - Phone",
    device = Devices.PIXEL_4,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
private fun WeatherWidgetPortraitContentComposablePhonePreview() {
    MaterialTheme {
        Surface {
            WeatherWidgetPortraitContentComposable(
                temperature = 72,
                shortForecast = "Sunny",
                icon = "https://api.weather.gov/icons/land/day/skc?size=medium",
                onRefresh = { },
                isFireTV = false
            )
        }
    }
}

@Preview(
    name = "Portrait Layout - Tablet",
    device = Devices.PIXEL_C,
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
private fun WeatherWidgetPortraitContentComposableTabletPreview() {
    MaterialTheme {
        Surface {
            WeatherWidgetPortraitContentComposable(
                temperature = 68,
                shortForecast = "Partly Cloudy",
                icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                onRefresh = { },
                isFireTV = false
            )
        }
    }
}

@Preview(
    name = "Portrait Layout - FireTV",
    device = "spec:width=1080dp,height=1920dp,dpi=320",
    showBackground = true,
    backgroundColor = 0xFF121212
)
@Composable
private fun WeatherWidgetPortraitContentComposableFireTVPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Surface {
            WeatherWidgetPortraitContentComposable(
                temperature = 78,
                shortForecast = "Clear",
                icon = "https://api.weather.gov/icons/land/night/skc?size=medium",
                onRefresh = { },
                isFireTV = true
            )
        }
    }
}