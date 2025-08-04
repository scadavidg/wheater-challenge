package com.domain.usecase

import com.domain.models.Result
import com.domain.repository.WeatherRepository

class GetTodayTemperatureUseCase(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(): Result<Int> {
        return weatherRepository.getTodayTemperature()
    }
}