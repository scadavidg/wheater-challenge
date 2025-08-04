package com.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
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

/**
 * Portrait layout content for Weather Widget
 * 
 * Optimized for vertical space with centered content alignment.
 * Adapts sizing and spacing based on platform (Android vs FireTV).
 */
@Composable
fun WeatherWidgetPortraitContentComposable(
    temperature: Int,
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
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "San Jose Weather",
            style = if (isFireTV) 
                MaterialTheme.typography.headlineMedium 
            else MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(if (isFireTV) 16.dp else 8.dp))
        
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
        
        Spacer(modifier = Modifier.height(if (isFireTV) 16.dp else 8.dp))
        
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
                onRefresh = { },
                isFireTV = true
            )
        }
    }
}