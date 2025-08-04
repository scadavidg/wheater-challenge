package com.data.models

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("ForecastResponse Model Tests")
class ForecastResponseTest {
    private lateinit var moshi: Moshi
    private lateinit var adapter: JsonAdapter<ForecastResponse>

    @BeforeEach
    fun setup() {
        // Given
        moshi =
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        adapter = moshi.adapter(ForecastResponse::class.java)
    }

    @Test
    @DisplayName("Given valid JSON response when parsing forecast then creates ForecastResponse correctly")
    fun givenValidJsonResponse_whenParsingForecast_thenCreatesForecastResponseCorrectly() {
        // Given
        val json =
            """
            {
                "properties": {
                    "periods": [
                        {"temperature": 75},
                        {"temperature": 68}
                    ]
                }
            }
            """.trimIndent()

        // When
        val forecastResponse = adapter.fromJson(json)

        // Then
        assertNotNull(forecastResponse)
        forecastResponse?.let {
            assertNotNull(forecastResponse.properties)
            assertEquals(2, forecastResponse.properties.periods.size)
            assertEquals(75, forecastResponse.properties.periods[0].temperature)
            assertEquals(68, forecastResponse.properties.periods[1].temperature)
        }
    }

    @Test
    @DisplayName("Given JSON with single period when parsing forecast then extracts temperature correctly")
    fun givenJsonWithSinglePeriod_whenParsingForecast_thenExtractsTemperatureCorrectly() {
        // Given
        val json =
            """
            {
                "properties": {
                    "periods": [
                        {"temperature": 82}
                    ]
                }
            }
            """.trimIndent()

        // When
        val forecastResponse = adapter.fromJson(json)

        // Then
        assertNotNull(forecastResponse)
        forecastResponse?.let {
            assertEquals(1, forecastResponse.properties.periods.size)
            assertEquals(82, forecastResponse.properties.periods[0].temperature)
        }
    }

    @Test
    @DisplayName("Given JSON with empty periods when parsing forecast then creates empty periods list")
    fun givenJsonWithEmptyPeriods_whenParsingForecast_thenCreatesEmptyPeriodsList() {
        // Given
        val json =
            """
            {
                "properties": {
                    "periods": []
                }
            }
            """.trimIndent()

        // When
        val forecastResponse = adapter.fromJson(json)

        // Then
        assertNotNull(forecastResponse)
        forecastResponse?.let {
            assertEquals(0, forecastResponse.properties.periods.size)
        }
    }

    @Test
    @DisplayName("Given ForecastResponse when serializing to JSON then produces valid JSON")
    fun givenForecastResponse_whenSerializingToJson_thenProducesValidJson() {
        // Given
        val period = Period(temperature = 72)
        val properties = ForecastProperties(periods = listOf(period))
        val forecastResponse = ForecastResponse(properties = properties)

        // When
        val json = adapter.toJson(forecastResponse)

        // Then
        assertNotNull(json)
        assert(json.contains("\"temperature\":72"))
        assert(json.contains("properties"))
        assert(json.contains("periods"))
    }

    @Test
    @DisplayName("Given negative temperature in JSON when parsing forecast then preserves negative value")
    fun givenNegativeTemperatureInJson_whenParsingForecast_thenPreservesNegativeValue() {
        // Given
        val json =
            """
            {
                "properties": {
                    "periods": [
                        {"temperature": -10}
                    ]
                }
            }
            """.trimIndent()

        // When
        val forecastResponse = adapter.fromJson(json)

        // Then
        assertNotNull(forecastResponse)
        forecastResponse?.let {
            assertEquals(-10, forecastResponse.properties.periods[0].temperature)
        }
    }
}
