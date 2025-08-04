package com.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Landscape layout content for Weather Widget
 * 
 * Optimized for horizontal space with side-by-side content and controls.
 * Maximizes efficient use of wide screen real estate.
 */
@Composable
fun WeatherWidgetLandscapeContentComposable(
    temperature: Int,
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
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "San Jose Weather",
                style = if (isFireTV) 
                    MaterialTheme.typography.headlineMedium 
                else MaterialTheme.typography.headlineSmall
            )
            
            Spacer(modifier = Modifier.height(if (isFireTV) 12.dp else 8.dp))
            
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
                }
            }
        }
        
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
                onRefresh = { },
                isFireTV = true
            )
        }
    }
}