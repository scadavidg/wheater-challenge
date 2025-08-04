package com.domain.repository

import com.domain.models.Weather
import com.domain.models.Result

interface WeatherRepository {
    suspend fun getTodayTemperature(): Result<Int>
    suspend fun getWeather(): Result<Weather>
}