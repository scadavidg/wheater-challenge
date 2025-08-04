package com.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter

/**
 * Landscape layout content for Weather Widget
 * 
 * Optimized for horizontal space with side-by-side content and controls.
 * Maximizes efficient use of wide screen real estate.
 * Displays temperature, weather condition, and weather icon in horizontal layout.
 */
@Composable
fun WeatherWidgetLandscapeContentComposable(
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
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(padding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side: Weather info
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Weather Icon with error handling
            if (!isLoading && !isError && icon.isNotEmpty()) {
                AsyncImage(
                    model = icon,
                    contentDescription = "Weather icon for $shortForecast",
                    modifier = Modifier.size(if (isFireTV) 72.dp else 56.dp),
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
                    modifier = Modifier.size(if (isFireTV) 54.dp else 42.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            }
            
            // Consistent spacing after icon section
            if (!isLoading && !isError && (icon.isNotEmpty() || icon.isEmpty())) {
                Spacer(modifier = Modifier.width(if (isFireTV) 16.dp else 12.dp))
            }
            
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "San Jose Weather",
                    style = if (isFireTV) 
                        MaterialTheme.typography.headlineMedium 
                    else MaterialTheme.typography.headlineSmall
                )
                
                Spacer(modifier = Modifier.height(if (isFireTV) 8.dp else 4.dp))
                
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(if (isFireTV) 48.dp else 36.dp)
                        )
                    }
                    isError -> {
                        Text(
                            text = "Error loading weather",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    else -> {
                        Text(
                            text = "${temperature}°F",
                            style = if (isFireTV) 
                                MaterialTheme.typography.displayLarge 
                            else MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        // Weather Description
                        if (shortForecast.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(if (isFireTV) 4.dp else 2.dp))
                            Text(
                                text = shortForecast,
                                style = if (isFireTV) 
                                    MaterialTheme.typography.titleMedium 
                                else MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            }
        }
        
        // Right side: Refresh button
        IconButton(
            onClick = onRefresh,
            modifier = Modifier.size(if (isFireTV) 56.dp else 48.dp)
        ) {
            Icon(
                Icons.Default.Refresh, 
                contentDescription = "Refresh",
                modifier = Modifier.size(if (isFireTV) 32.dp else 24.dp)
            )
        }
    }
}

// ===== LAYOUT-SPECIFIC PREVIEWS =====
// Focus: Landscape layout behavior across form factors

@Preview(
    name = "Landscape Layout - Phone",
    device = "spec:width=891dp,height=411dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape",
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
private fun WeatherWidgetLandscapeContentComposablePhonePreview() {
    MaterialTheme {
        Surface {
            WeatherWidgetLandscapeContentComposable(
                temperature = 85,
                shortForecast = "Hot",
                icon = "https://api.weather.gov/icons/land/day/hot?size=medium",
                onRefresh = { },
                isFireTV = false
            )
        }
    }
}

@Preview(
    name = "Landscape Layout - Tablet",
    device = "spec:width=1280dp,height=900dp,dpi=240,isRound=false,chinSize=0dp,orientation=landscape",
    showBackground = true,
    backgroundColor = 0xFFF5F5F5
)
@Composable
private fun WeatherWidgetLandscapeContentComposableTabletPreview() {
    MaterialTheme {
        Surface {
            WeatherWidgetLandscapeContentComposable(
                temperature = 75,
                shortForecast = "Partly Cloudy",
                icon = "https://api.weather.gov/icons/land/day/bkn?size=medium",
                onRefresh = { },
                isFireTV = false
            )
        }
    }
}

@Preview(
    name = "Landscape Layout - FireTV",
    device = "spec:width=1920dp,height=1080dp,dpi=320",
    showBackground = true,
    backgroundColor = 0xFF121212
)
@Composable
private fun WeatherWidgetLandscapeContentComposableFireTVPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Surface {
            WeatherWidgetLandscapeContentComposable(
                temperature = 78,
                shortForecast = "Clear",
                icon = "https://api.weather.gov/icons/land/night/skc?size=medium",
                onRefresh = { },
                isFireTV = true
            )
        }
    }
}