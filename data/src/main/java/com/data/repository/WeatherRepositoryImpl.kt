package com.data.repository

import com.data.api.WeatherApi
import com.data.config.ApiConfig.DEFAULT_LATITUDE
import com.data.config.ApiConfig.DEFAULT_LONGITUDE
import com.data.models.WeatherData
import com.domain.models.Result
import com.domain.models.Weather
import com.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl
    @Inject
    constructor(
        private val api: WeatherApi,
    ) : WeatherRepository {
        override suspend fun getTodayTemperature(): Result<Int> {
            return try {
                val pointsResponse = api.getWeatherPoints(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
                val forecastUrl = pointsResponse.properties.forecast
                val forecastResponse = api.getForecast(forecastUrl)
                val temperature = forecastResponse.properties.periods[0].temperature
                Result.Success(temperature)
            } catch (e: Exception) {
                Result.Error(e.message ?: "Unknown error occurred")
            }
        }

        override suspend fun getWeather(): Result<Weather> {
            return try {
                val temperature = getTodayTemperature()
                when (temperature) {
                    is Result.Success -> {
                        // Map data layer model to domain model
                        val weatherData = WeatherData(temperature.data)
                        Result.Success(weatherData.mapToDomain())
                    }

                    is Result.Error -> Result.Error(temperature.message)
                    is Result.Loading -> Result.Loading
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
