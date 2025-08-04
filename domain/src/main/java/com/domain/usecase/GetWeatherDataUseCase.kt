package com.domain.usecase

import com.domain.models.Result
import com.domain.models.Weather
import com.domain.repository.WeatherRepository

/**
 * Use case to get complete weather data including temperature, forecast, and icon
 */
class GetWeatherDataUseCase(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(): Result<Weather> {
        return weatherRepository.getWeather()
    }
}