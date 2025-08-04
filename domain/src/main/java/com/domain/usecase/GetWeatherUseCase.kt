package com.domain.usecase

import com.domain.models.Weather
import com.domain.models.Result
import com.domain.repository.WeatherRepository

class GetWeatherUseCase(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(): Result<Weather> {
        return weatherRepository.getWeather()
    }
}