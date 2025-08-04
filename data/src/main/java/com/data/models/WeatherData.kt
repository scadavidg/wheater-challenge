package com.data.models

import com.domain.models.Weather

data class WeatherData(
    val temperature: Int,
    val location: String = "San Jose, CA",
) {
    fun mapToDomain(): Weather {
        return Weather(
            temperature = temperature,
            location = location,
        )
    }
}
