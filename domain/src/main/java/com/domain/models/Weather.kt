package com.domain.models

data class Weather(
    val temperature: Int,
    val shortForecast: String,
    val icon: String,
    val location: String = "San Jose, CA"
)
