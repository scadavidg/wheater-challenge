package com.widget

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Main Weather Widget Composable - Entry point for weather display
 * 
 * Adaptively chooses between portrait and landscape layouts based on orientation.
 * Supports both Google Android and Amazon FireTV variants with platform-specific optimizations.
 * 
 * @param temperature Temperature in Fahrenheit to display
 * @param modifier Compose modifier for styling
 * @param onRefresh Callback when refresh button is pressed
 * @param isLoading Shows loading state when true
 * @param isError Shows error state when true  
 * @param isLandscape Switches to landscape layout when true
 * @param isFireTV Applies FireTV optimizations when true
 */
@Composable
fun WeatherWidgetComposable(
    temperature: Int,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    isLoading: Boolean = false,
    isError: Boolean = false,
    isLandscape: Boolean = false,
    isFireTV: Boolean = false,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isFireTV) 8.dp else 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isFireTV) 
                MaterialTheme.colorScheme.surfaceVariant 
            else MaterialTheme.colorScheme.surface
        )
    ) {
        if (isLandscape) {
            WeatherWidgetLandscapeContentComposable(
                temperature = temperature,
                onRefresh = onRefresh,
                isLoading = isLoading,
                isError = isError,
                isFireTV = isFireTV
            )
        } else {
            WeatherWidgetPortraitContentComposable(
                temperature = temperature,
                onRefresh = onRefresh,
                isLoading = isLoading,
                isError = isError,
                isFireTV = isFireTV
            )
        }
    }
}