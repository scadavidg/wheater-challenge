package com.domain.models

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

@DisplayName("Weather Model Tests")
class WeatherTest {

    @Test
    @DisplayName("Given temperature only when creating Weather then has default location")
    fun givenTemperatureOnly_whenCreatingWeather_thenHasDefaultLocation() {
        // Given
        val temperature = 25
        
        // When
        val weather = Weather(
            temperature = temperature,
            shortForecast = "Sunny",
            icon = "https://api.weather.gov/icons/land/day/skc?size=medium"
        )
        
        // Then
        assertEquals(temperature, weather.temperature)
        assertEquals("Sunny", weather.shortForecast)
        assertEquals("https://api.weather.gov/icons/land/day/skc?size=medium", weather.icon)
        assertEquals("San Jose, CA", weather.location)
    }

    @Test
    @DisplayName("Given temperature and custom location when creating Weather then preserves both")
    fun givenTemperatureAndCustomLocation_whenCreatingWeather_thenPreservesBoth() {
        // Given
        val temperature = 30
        val customLocation = "Miami, FL"
        
        // When
        val weather = Weather(
            temperature = temperature,
            shortForecast = "Hot",
            icon = "https://api.weather.gov/icons/land/day/hot?size=medium",
            location = customLocation
        )
        
        // Then
        assertEquals(temperature, weather.temperature)
        assertEquals("Hot", weather.shortForecast)
        assertEquals("https://api.weather.gov/icons/land/day/hot?size=medium", weather.icon)
        assertEquals(customLocation, weather.location)
    }

    @Test
    @DisplayName("Given negative temperature when creating Weather then accepts negative value")
    fun givenNegativeTemperature_whenCreatingWeather_thenAcceptsNegativeValue() {
        // Given
        val negativeTemperature = -10
        
        // When
        val weather = Weather(
            temperature = negativeTemperature,
            shortForecast = "Snow",
            icon = "https://api.weather.gov/icons/land/day/snow?size=medium"
        )
        
        // Then
        assertEquals(negativeTemperature, weather.temperature)
        assertEquals("Snow", weather.shortForecast)
        assertEquals("https://api.weather.gov/icons/land/day/snow?size=medium", weather.icon)
        assertEquals("San Jose, CA", weather.location)
    }

    @Test
    @DisplayName("Given two identical Weather objects when comparing then are equal")
    fun givenTwoIdenticalWeatherObjects_whenComparing_thenAreEqual() {
        // Given
        val weather1 = Weather(
            temperature = 25,
            shortForecast = "Clear",
            icon = "https://api.weather.gov/icons/land/day/few?size=medium",
            location = "Test City"
        )
        val weather2 = Weather(
            temperature = 25,
            shortForecast = "Clear",
            icon = "https://api.weather.gov/icons/land/day/few?size=medium",
            location = "Test City"
        )
        
        // When & Then
        assertEquals(weather1, weather2)
        assertEquals(weather1.hashCode(), weather2.hashCode())
    }
}