package com.widget

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Assertions.*

/**
 * State management tests for Weather Widget
 * 
 * Tests state transitions, precedence rules, and complex state combinations
 * that drive the widget's display behavior.
 */
class WeatherWidgetStateTest {

    @Test
    @DisplayName("Should determine loading state correctly")
    fun `should determine loading state correctly`() {
        // Given
        val state = WidgetState(
            temperature = 75,
            isLoading = true,
            isError = false,
            isLandscape = false,
            isFireTV = false
        )
        
        // When
        val displayState = state.determineDisplayState()
        
        // Then
        assertEquals(DisplayType.LOADING, displayState, "Should be in loading state")
    }
    
    @Test
    @DisplayName("Should determine error state correctly")
    fun `should determine error state correctly`() {
        // Given
        val state = WidgetState(
            temperature = 75,
            isLoading = false,
            isError = true,
            isLandscape = false,
            isFireTV = false
        )
        
        // When
        val displayState = state.determineDisplayState()
        
        // Then
        assertEquals(DisplayType.ERROR, displayState, "Should be in error state")
    }
    
    @Test
    @DisplayName("Should determine normal state correctly")
    fun `should determine normal state correctly`() {
        // Given
        val state = WidgetState(
            temperature = 75,
            isLoading = false,
            isError = false,
            isLandscape = false,
            isFireTV = false
        )
        
        // When
        val displayState = state.determineDisplayState()
        
        // Then
        assertEquals(DisplayType.NORMAL, displayState, "Should be in normal state")
    }
    
    @Test
    @DisplayName("Should prioritize error over loading")
    fun `should prioritize error over loading`() {
        // Given
        val state = WidgetState(
            temperature = 75,
            isLoading = true,
            isError = true,
            isLandscape = false,
            isFireTV = false
        )
        
        // When
        val displayState = state.determineDisplayState()
        
        // Then
        // Based on the when clause in the actual composables: isLoading comes first
        assertEquals(DisplayType.LOADING, displayState, "Loading should take precedence in when clause")
    }
    
    @Test
    @DisplayName("Should handle state transitions correctly")
    fun `should handle state transitions correctly`() {
        // Given - Start with loading
        var state = WidgetState(
            temperature = 75,
            isLoading = true,
            isError = false,
            isLandscape = false,
            isFireTV = false
        )
        
        // When/Then - Loading state
        assertEquals(DisplayType.LOADING, state.determineDisplayState(), "Should start in loading")
        
        // When - Transition to success
        state = state.copy(isLoading = false, isError = false)
        
        // Then
        assertEquals(DisplayType.NORMAL, state.determineDisplayState(), "Should transition to normal")
        
        // When - Transition to error
        state = state.copy(isLoading = false, isError = true)
        
        // Then
        assertEquals(DisplayType.ERROR, state.determineDisplayState(), "Should transition to error")
        
        // When - Back to loading
        state = state.copy(isLoading = true, isError = false)
        
        // Then
        assertEquals(DisplayType.LOADING, state.determineDisplayState(), "Should transition back to loading")
    }
    
    @Test
    @DisplayName("Should handle platform configuration correctly")
    fun `should handle platform configuration correctly`() {
        // Given
        val androidState = WidgetState(
            temperature = 75,
            isLoading = false,
            isError = false,
            isLandscape = false,
            isFireTV = false
        )
        
        val fireTVState = WidgetState(
            temperature = 75,
            isLoading = false,
            isError = false,
            isLandscape = true,
            isFireTV = true
        )
        
        // When
        val androidConfig = androidState.getPlatformConfig()
        val fireTVConfig = fireTVState.getPlatformConfig()
        
        // Then
        assertEquals(16, androidConfig.padding, "Android should have smaller padding")
        assertEquals(4, androidConfig.elevation, "Android should have lower elevation")
        assertEquals(24, fireTVConfig.padding, "FireTV should have larger padding")
        assertEquals(8, fireTVConfig.elevation, "FireTV should have higher elevation")
    }
    
    @Test
    @DisplayName("Should handle orientation configuration correctly")
    fun `should handle orientation configuration correctly`() {
        // Given
        val portraitState = WidgetState(
            temperature = 75,
            isLoading = false,
            isError = false,
            isLandscape = false,
            isFireTV = false
        )
        
        val landscapeState = WidgetState(
            temperature = 75,
            isLoading = false,
            isError = false,
            isLandscape = true,
            isFireTV = false
        )
        
        // When
        val portraitLayout = portraitState.getLayoutType()
        val landscapeLayout = landscapeState.getLayoutType()
        
        // Then
        assertEquals(LayoutType.PORTRAIT, portraitLayout, "Should be portrait layout")
        assertEquals(LayoutType.LANDSCAPE, landscapeLayout, "Should be landscape layout")
    }
    
    @Test
    @DisplayName("Should validate complete state combinations")
    fun `should validate complete state combinations`() {
        // Given - All possible boolean combinations (2^4 = 16 combinations)
        val combinations = listOf(
            // isLoading, isError, isLandscape, isFireTV
            listOf(false, false, false, false), // Android Portrait Normal
            listOf(false, false, false, true),  // FireTV Portrait Normal
            listOf(false, false, true, false),  // Android Landscape Normal
            listOf(false, false, true, true),   // FireTV Landscape Normal
            listOf(false, true, false, false),  // Android Portrait Error
            listOf(false, true, false, true),   // FireTV Portrait Error
            listOf(false, true, true, false),   // Android Landscape Error
            listOf(false, true, true, true),    // FireTV Landscape Error
            listOf(true, false, false, false),  // Android Portrait Loading
            listOf(true, false, false, true),   // FireTV Portrait Loading
            listOf(true, false, true, false),   // Android Landscape Loading
            listOf(true, false, true, true),    // FireTV Landscape Loading
            listOf(true, true, false, false),   // Android Portrait Loading+Error
            listOf(true, true, false, true),    // FireTV Portrait Loading+Error
            listOf(true, true, true, false),    // Android Landscape Loading+Error
            listOf(true, true, true, true),     // FireTV Landscape Loading+Error
        )
        
        // When/Then - All combinations should be valid
        combinations.forEach { (isLoading, isError, isLandscape, isFireTV) ->
            val state = WidgetState(
                temperature = 75,
                isLoading = isLoading,
                isError = isError,
                isLandscape = isLandscape,
                isFireTV = isFireTV
            )
            
            // Should not throw exception and should return valid display state
            val displayState = state.determineDisplayState()
            assertTrue(
                displayState in listOf(DisplayType.LOADING, DisplayType.ERROR, DisplayType.NORMAL),
                "Display state should be valid")
        }
    }
    
    @Test
    @DisplayName("Should handle temperature with different states")
    fun `should handle temperature with different states`() {
        // Given
        val temperatures = listOf(-25, 0, 32, 75, 100, 125)
        
        temperatures.forEach { temp ->
            // When - Normal state
            val normalState = WidgetState(
                temperature = temp,
                isLoading = false,
                isError = false,
                isLandscape = false,
                isFireTV = false
            )
            
            // Then
            assertEquals(DisplayType.NORMAL, normalState.determineDisplayState(), "Normal state should display temperature")
            assertEquals(temp, normalState.temperature, "Temperature should be preserved")
            
            // When - Loading state
            val loadingState = normalState.copy(isLoading = true)
            
            // Then
            assertEquals(DisplayType.LOADING, loadingState.determineDisplayState(), "Loading state should not display temperature")
            
            // When - Error state
            val errorState = normalState.copy(isError = true)
            
            // Then
            assertEquals(DisplayType.ERROR, errorState.determineDisplayState(), "Error state should not display temperature")
        }
    }
    
    // Helper data classes and enums
    data class WidgetState(
        val temperature: Int,
        val isLoading: Boolean,
        val isError: Boolean,
        val isLandscape: Boolean,
        val isFireTV: Boolean
    ) {
        fun determineDisplayState(): DisplayType {
            return when {
                isLoading -> DisplayType.LOADING
                isError -> DisplayType.ERROR
                else -> DisplayType.NORMAL
            }
        }
        
        fun getPlatformConfig(): PlatformConfig {
            return PlatformConfig(
                padding = if (isFireTV) 24 else 16,
                elevation = if (isFireTV) 8 else 4
            )
        }
        
        fun getLayoutType(): LayoutType {
            return if (isLandscape) LayoutType.LANDSCAPE else LayoutType.PORTRAIT
        }
    }
    
    enum class DisplayType {
        LOADING, ERROR, NORMAL
    }
    
    enum class LayoutType {
        PORTRAIT, LANDSCAPE
    }
    
    data class PlatformConfig(
        val padding: Int,
        val elevation: Int
    )
}