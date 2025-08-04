package com.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ForecastResponse(
    @Json(name = "properties")
    val properties: ForecastProperties,
)

@JsonClass(generateAdapter = true)
data class ForecastProperties(
    @Json(name = "periods")
    val periods: List<Period>,
)

@JsonClass(generateAdapter = true)
data class Period(
    @Json(name = "temperature")
    val temperature: Int,
)
