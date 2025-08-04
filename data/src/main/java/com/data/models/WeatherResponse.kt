package com.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "properties")
    val properties: WeatherProperties,
)

@JsonClass(generateAdapter = true)
data class WeatherProperties(
    @Json(name = "forecast")
    val forecast: String,
)
