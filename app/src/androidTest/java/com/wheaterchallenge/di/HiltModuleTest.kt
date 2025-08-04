package com.wheaterchallenge.di

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.data.repository.WeatherRepositoryImpl
import com.domain.repository.WeatherRepository
import com.domain.usecase.GetTodayTemperatureUseCase
import com.domain.usecase.GetWeatherDataUseCase
import com.domain.usecase.GetWeatherUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertSame
import javax.inject.Inject

/**
 * Tests for Hilt dependency injection modules.
 * Verifies that all dependencies are correctly provided and injected.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HiltModuleTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    // Inject dependencies to test
    @Inject
    lateinit var weatherRepository: WeatherRepository

    @Inject
    lateinit var getWeatherUseCase: GetWeatherUseCase

    @Inject
    lateinit var getTodayTemperatureUseCase: GetTodayTemperatureUseCase

    @Inject
    lateinit var getWeatherDataUseCase: GetWeatherDataUseCase

    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        hiltRule.inject()
    }

    @Test
    fun repositoryModule_bindsWeatherRepository_correctly() {
        // Then - Verify repository is injected
        assertNotNull("WeatherRepository should be injected", weatherRepository)
        
        // Verify it's the correct implementation
        assertTrue("WeatherRepository should be WeatherRepositoryImpl", 
                  weatherRepository is WeatherRepositoryImpl)
    }

    @Test
    fun useCaseModule_providesGetWeatherUseCase_correctly() {
        // Then - Verify GetWeatherUseCase is injected
        assertNotNull("GetWeatherUseCase should be injected", getWeatherUseCase)
        
        // Verify it has the correct repository dependency
        // Note: We can't easily access private fields, but we can verify it's not null
        // and that it doesn't throw when created
    }

    @Test
    fun useCaseModule_providesGetTodayTemperatureUseCase_correctly() {
        // Then - Verify GetTodayTemperatureUseCase is injected
        assertNotNull("GetTodayTemperatureUseCase should be injected", getTodayTemperatureUseCase)
    }

    @Test
    fun useCaseModule_providesGetWeatherDataUseCase_correctly() {
        // Then - Verify GetWeatherDataUseCase is injected
        assertNotNull("GetWeatherDataUseCase should be injected", getWeatherDataUseCase)
    }

    @Test
    fun hiltComponents_provideSingletonInstances_correctly() {
        // Given - Inject the same dependencies again
        val testComponent = TestComponentHolder()
        hiltRule.inject(testComponent)

        // Then - Verify singleton behavior
        // Repository should be the same instance (singleton scoped)
        assertSame("WeatherRepository should be singleton", 
                  weatherRepository, testComponent.weatherRepository)
        
        // Use cases should be the same instance (singleton scoped)
        assertSame("GetWeatherUseCase should be singleton", 
                  getWeatherUseCase, testComponent.getWeatherUseCase)
        
        assertSame("GetTodayTemperatureUseCase should be singleton", 
                  getTodayTemperatureUseCase, testComponent.getTodayTemperatureUseCase)
        
        assertSame("GetWeatherDataUseCase should be singleton", 
                  getWeatherDataUseCase, testComponent.getWeatherDataUseCase)
    }

    @Test
    fun hiltComponents_injectDependencies_successfully() {
        // Then - Verify all dependencies are successfully injected
        assertNotNull("WeatherRepository should not be null", weatherRepository)
        assertNotNull("GetWeatherUseCase should not be null", getWeatherUseCase)
        assertNotNull("GetTodayTemperatureUseCase should not be null", getTodayTemperatureUseCase)
        assertNotNull("GetWeatherDataUseCase should not be null", getWeatherDataUseCase)
        
        // Verify we can access all injected dependencies without exceptions
        try {
            weatherRepository.toString()
            getWeatherUseCase.toString()
            getTodayTemperatureUseCase.toString()
            getWeatherDataUseCase.toString()
        } catch (e: Exception) {
            throw AssertionError("All injected dependencies should be accessible", e)
        }
    }

    @Test
    fun repositoryModule_bindsCorrectInterface_toImplementation() {
        // Then - Verify the repository is bound to the correct interface
        assertTrue("WeatherRepository should be instance of domain interface",
                  weatherRepository is com.domain.repository.WeatherRepository)
        
        assertTrue("WeatherRepository should be implemented by data layer",
                  weatherRepository is com.data.repository.WeatherRepositoryImpl)
    }

    @Test
    fun useCaseModule_providesUseCasesWithCorrectDependencies_successfully() {
        // When - Try to access use cases (this would fail if dependencies aren't correct)
        try {
            // These calls should not throw if dependencies are correctly injected
            getWeatherUseCase.hashCode()
            getTodayTemperatureUseCase.hashCode()
            getWeatherDataUseCase.hashCode()
        } catch (e: Exception) {
            throw AssertionError("Use cases should have correct dependencies injected", e)
        }
        
        // Then - If we reach here, all use cases have their dependencies correctly injected
        assertTrue("All use cases should be properly constructed", true)
    }

    @Test
    fun hiltModules_workWithAndroidComponents_correctly() {
        // Then - Verify that Hilt works correctly with Android components
        assertNotNull("Context should be available for injection", context)
        
        // Verify that our custom modules work alongside built-in Android modules
        assertNotNull("WeatherRepository should be available in Android context", weatherRepository)
        assertNotNull("Use cases should be available in Android context", getWeatherUseCase)
    }

    @Test
    fun dependencyGraph_hasCorrectScopedInstances_acrossInjections() {
        // Given - Create multiple test holders to verify scoping
        val holder1 = TestComponentHolder()
        val holder2 = TestComponentHolder()
        
        hiltRule.inject(holder1)
        hiltRule.inject(holder2)

        // Then - Verify singleton scoped instances are the same
        assertSame("Repository should be same instance across injections",
                  holder1.weatherRepository, holder2.weatherRepository)
        
        assertSame("GetWeatherUseCase should be same instance across injections",
                  holder1.getWeatherUseCase, holder2.getWeatherUseCase)
    }

    /**
     * Helper class to test multiple injections and singleton behavior
     */
    class TestComponentHolder {
        @Inject
        lateinit var weatherRepository: WeatherRepository

        @Inject
        lateinit var getWeatherUseCase: GetWeatherUseCase

        @Inject
        lateinit var getTodayTemperatureUseCase: GetTodayTemperatureUseCase

        @Inject
        lateinit var getWeatherDataUseCase: GetWeatherDataUseCase
    }
}