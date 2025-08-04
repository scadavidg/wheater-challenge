package com.data.api

import com.data.models.ForecastResponse
import com.data.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface WeatherApi {
    @GET("points/{lat},{lon}")
    suspend fun getWeatherPoints(
        @Path("lat") lat: Double,
        @Path("lon") lon: Double,
    ): WeatherResponse

    @GET
    suspend fun getForecast(
        @Url url: String,
    ): ForecastResponse
}
