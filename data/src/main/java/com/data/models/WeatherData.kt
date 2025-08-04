package com.data.models

import com.domain.models.Weather

data class WeatherData(
    val temperature: Int,
    val shortForecast: String,
    val icon: String,
    val location: String = "San Jose, CA",
) {
    fun mapToDomain(): Weather {
        return Weather(
            temperature = temperature,
            shortForecast = shortForecast,
            icon = icon,
            location = location,
        )
    }
}
