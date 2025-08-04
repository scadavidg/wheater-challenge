package com.domain.models

data class Weather(
    val temperature: Int,
    val location: String = "San Jose, CA"
)
