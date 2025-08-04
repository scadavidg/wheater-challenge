package com.wheaterchallenge.di

import com.domain.repository.WeatherRepository
import com.domain.usecase.GetTodayTemperatureUseCase
import com.domain.usecase.GetWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetWeatherUseCase(repository: WeatherRepository): GetWeatherUseCase = GetWeatherUseCase(repository)

    @Provides
    @Singleton
    fun provideGetTodayTemperatureUseCase(repository: WeatherRepository): GetTodayTemperatureUseCase = GetTodayTemperatureUseCase(repository)
}
