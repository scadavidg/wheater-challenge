package com.data.models

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("WeatherResponse Model Tests")
class WeatherResponseTest {
    private lateinit var moshi: Moshi
    private lateinit var adapter: JsonAdapter<WeatherResponse>

    @BeforeEach
    fun setup() {
        // Given
        moshi =
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        adapter = moshi.adapter(WeatherResponse::class.java)
    }

    @Test
    @DisplayName("Given valid JSON response when parsing weather points then creates WeatherResponse correctly")
    fun givenValidJsonResponse_whenParsingWeatherPoints_thenCreatesWeatherResponseCorrectly() {
        // Given
        val forecastUrl = "https://api.weather.gov/gridpoints/MTR/85,105/forecast"
        val json =
            """
            {
                "properties": {
                    "forecast": "$forecastUrl"
                }
            }
            """.trimIndent()

        // When
        val weatherResponse = adapter.fromJson(json)

        // Then
        assertNotNull(weatherResponse)
        weatherResponse?.let {
            assertNotNull(weatherResponse.properties)
            assertEquals(forecastUrl, weatherResponse.properties.forecast)
        }
    }

    @Test
    @DisplayName("Given JSON with empty forecast URL when parsing weather points then preserves empty URL")
    fun givenJsonWithEmptyForecastUrl_whenParsingWeatherPoints_thenPreservesEmptyUrl() {
        // Given
        val json =
            """
            {
                "properties": {
                    "forecast": ""
                }
            }
            """.trimIndent()

        // When
        val weatherResponse = adapter.fromJson(json)

        // Then
        assertNotNull(weatherResponse)
        assertEquals("", weatherResponse?.properties?.forecast)
    }

    @Test
    @DisplayName("Given WeatherResponse when serializing to JSON then produces valid JSON")
    fun givenWeatherResponse_whenSerializingToJson_thenProducesValidJson() {
        // Given
        val forecastUrl = "https://api.weather.gov/gridpoints/TEST/1,2/forecast"
        val properties = WeatherProperties(forecast = forecastUrl)
        val weatherResponse = WeatherResponse(properties = properties)

        // When
        val json = adapter.toJson(weatherResponse)

        // Then
        assertNotNull(json)
        assert(json.contains("\"forecast\":\"$forecastUrl\""))
        assert(json.contains("properties"))
    }

    @Test
    @DisplayName("Given complex forecast URL when parsing weather points then extracts URL correctly")
    fun givenComplexForecastUrl_whenParsingWeatherPoints_thenExtractsUrlCorrectly() {
        // Given
        val complexUrl = "https://api.weather.gov/gridpoints/MTR/85,105/forecast?units=us&param1=value1"
        val json =
            """
            {
                "properties": {
                    "forecast": "$complexUrl"
                }
            }
            """.trimIndent()

        // When
        val weatherResponse = adapter.fromJson(json)

        // Then
        assertNotNull(weatherResponse)
        assertEquals(complexUrl, weatherResponse?.properties?.forecast)
    }

    @Test
    @DisplayName("Given WeatherResponse with forecast URL when getting properties then returns correct properties")
    fun givenWeatherResponseWithForecastUrl_whenGettingProperties_thenReturnsCorrectProperties() {
        // Given
        val expectedUrl = "https://api.weather.gov/test/forecast"
        val properties = WeatherProperties(forecast = expectedUrl)
        val weatherResponse = WeatherResponse(properties = properties)

        // When
        val actualProperties = weatherResponse.properties

        // Then
        assertEquals(expectedUrl, actualProperties.forecast)
        assertEquals(properties, actualProperties)
    }
}
