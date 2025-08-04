package com.data.models

import com.domain.models.Weather
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("WeatherData Mapper Tests")
class WeatherDataTest {
    @Test
    @DisplayName("Given WeatherData with custom location when mapping to domain then preserves all data")
    fun givenWeatherDataWithCustomLocation_whenMappingToDomain_thenPreservesAllData() {
        // Given
        val temperature = 28
        val location = "New York, NY"
        val weatherData = WeatherData(temperature = temperature, location = location)

        // When
        val domainWeather = weatherData.mapToDomain()

        // Then
        assertEquals(temperature, domainWeather.temperature)
        assertEquals(location, domainWeather.location)
    }

    @Test
    @DisplayName("Given WeatherData with default location when mapping to domain then uses default location")
    fun givenWeatherDataWithDefaultLocation_whenMappingToDomain_thenUsesDefaultLocation() {
        // Given
        val temperature = 23
        val weatherData = WeatherData(temperature = temperature) // Uses default location

        // When
        val domainWeather = weatherData.mapToDomain()

        // Then
        assertEquals(temperature, domainWeather.temperature)
        assertEquals("San Jose, CA", domainWeather.location)
    }

    @Test
    @DisplayName("Given WeatherData with negative temperature when mapping to domain then preserves negative temperature")
    fun givenWeatherDataWithNegativeTemperature_whenMappingToDomain_thenPreservesNegativeTemperature() {
        // Given
        val negativeTemperature = -5
        val location = "Alaska"
        val weatherData = WeatherData(temperature = negativeTemperature, location = location)

        // When
        val domainWeather = weatherData.mapToDomain()

        // Then
        assertEquals(negativeTemperature, domainWeather.temperature)
        assertEquals(location, domainWeather.location)
    }

    @Test
    @DisplayName("Given WeatherData when mapping to domain then returns Weather instance")
    fun givenWeatherData_whenMappingToDomain_thenReturnsWeatherInstance() {
        // Given
        val weatherData = WeatherData(temperature = 20, location = "Test Location")

        // When
        val result = weatherData.mapToDomain()

        // Then
        assert(result is Weather)
        assertEquals(weatherData.temperature, result.temperature)
        assertEquals(weatherData.location, result.location)
    }

    @Test
    @DisplayName("Given multiple WeatherData objects when mapping to domain then maintains independence")
    fun givenMultipleWeatherDataObjects_whenMappingToDomain_thenMaintainsIndependence() {
        // Given
        val weatherData1 = WeatherData(temperature = 15, location = "City A")
        val weatherData2 = WeatherData(temperature = 25, location = "City B")

        // When
        val domainWeather1 = weatherData1.mapToDomain()
        val domainWeather2 = weatherData2.mapToDomain()

        // Then
        assertEquals(15, domainWeather1.temperature)
        assertEquals("City A", domainWeather1.location)
        assertEquals(25, domainWeather2.temperature)
        assertEquals("City B", domainWeather2.location)
    }
}
