# Weather Challenge - Multi-Platform Android Weather Widget

A modern, adaptive weather application built with Jetpack Compose that displays current weather information for San Jose, CA. The app automatically adapts its UI for different devices and orientations, with specific optimizations for Google Android and Amazon FireTV platforms.

## 📋 Project Overview

This weather application demonstrates clean architecture principles, modern Android development practices, and multi-platform compatibility. It fetches real-time weather data from the National Weather Service API and presents it through a responsive, adaptive UI that works seamlessly across phones, tablets, and Android TV devices.

The project showcases advanced Android development concepts including Clean Architecture, dependency injection with Hilt, comprehensive testing strategies, and platform-specific optimizations for both Google Play Store and Amazon Appstore distributions.

## ✨ Key Features

### 🌤️ **Weather Display**
- **Real-time Weather**: Current temperature, forecast, and weather icons for San Jose, CA  
- **Dynamic Icons**: Weather condition icons loaded from National Weather Service API
- **Smart Fallbacks**: Graceful handling of image loading failures with fallback icons

### 📱 **Adaptive UI Design**
- **Responsive Layout**: Automatically adapts between portrait and landscape orientations
- **Multi-Device Support**: Optimized layouts for phones, tablets, and TV screens
- **Platform Detection**: Automatic FireTV detection with enhanced TV-friendly UI elements

### 🔄 **State Management**
- **Loading States**: Visual feedback during data fetching operations
- **Error Handling**: Comprehensive error states with retry functionality
- **Reactive UI**: Real-time state updates using Kotlin Flows and StateFlow

### 🎯 **Multi-Platform Support**
- **Google Android**: Optimized for Google Play Store distribution
- **Amazon FireTV**: Enhanced for Amazon Appstore with TV-specific optimizations
- **Product Flavors**: Separate build variants for different platforms

### 🧪 **Testing Excellence**
- **Unit Testing**: Comprehensive ViewModel and use case testing
- **Integration Testing**: API integration and repository testing  
- **UI Testing**: Compose UI testing with different device configurations
- **Error Scenario Testing**: Network failures and edge case handling

## 🏗️ Architecture

The application follows **Clean Architecture** principles with clear separation of concerns across three main layers:

```
┌─────────────────────────────────────────────────────────────┐
│                        APP LAYER                            │
│  ┌────────────────┐ ┌─────────────────┐ ┌─────────────────┐│
│  │   MainActivity │ │ WeatherViewModel│ │   DI Modules    ││
│  │   (Compose)    │ │   (StateFlow)   │ │   (Hilt)        ││
│  └────────────────┘ └─────────────────┘ └─────────────────┘│
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                      DOMAIN LAYER                           │
│  ┌────────────────┐ ┌─────────────────┐ ┌─────────────────┐│
│  │   Use Cases    │ │   Repository    │ │   Models        ││
│  │   (Business    │ │   Interface     │ │   (Weather,     ││
│  │    Logic)      │ │                 │ │    Result)      ││
│  └────────────────┘ └─────────────────┘ └─────────────────┘│
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                       DATA LAYER                            │
│  ┌────────────────┐ ┌─────────────────┐ ┌─────────────────┐│
│  │   Repository   │ │   Weather API   │ │   Data Models   ││
│  │ Implementation │ │   (Retrofit)    │ │   (DTOs)        ││
│  └────────────────┘ └─────────────────┘ └─────────────────┘│
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                      WIDGET MODULE                          │
│  ┌────────────────┐ ┌─────────────────┐ ┌─────────────────┐│
│  │   Adaptive     │ │   Portrait      │ │   Landscape     ││
│  │   Widget       │ │   Layout        │ │   Layout        ││
│  │  (Composable)  │ │                 │ │                 ││
│  └────────────────┘ └─────────────────┘ └─────────────────┘│
└─────────────────────────────────────────────────────────────┘
```

### **Layer Responsibilities:**

- **App Layer**: UI components, ViewModels, dependency injection configuration
- **Domain Layer**: Business logic, use cases, domain models, repository contracts
- **Data Layer**: API implementation, data models, repository implementation
- **Widget Module**: Reusable UI components with adaptive layouts

## 🛠️ Technologies Used

### **Core Framework**
- **Android SDK 36** with **Kotlin 2.2.0**
- **Jetpack Compose** (Material 3 Design System)
- **Kotlin Coroutines** & **Flow** for reactive programming

### **Architecture & DI**
- **MVVM Pattern** with Clean Architecture
- **Hilt (Dagger)** for dependency injection
- **StateFlow** for state management

### **Networking & Serialization**
- **Retrofit 3.0.0** for API communication
- **Moshi 1.15.2** for JSON serialization
- **OkHttp 4.12.0** with logging interceptor

### **UI & Image Loading**
- **Jetpack Compose BOM 2024.09.00**
- **Material 3** design components
- **Coil Compose 2.7.0** for image loading

### **Testing Framework**
- **JUnit 5.10.1** (modern testing)
- **JUnit 4.13.2** (legacy compatibility)
- **Mockito 5.7.0** & **Mockito-Kotlin 5.2.1**
- **Turbine 1.0.0** for Flow testing
- **Kotlin Coroutines Test 1.7.3**

### **Build Tools**
- **Android Gradle Plugin 8.11.1**
- **Kotlin Symbol Processing (KSP)**
- **ktlint** for code quality

## 📸 Demo - Multi-Device Screenshots

| Device Type | Portrait | Landscape |
|------------|----------|-----------|
| **Phone (Medium)** | <img src="screenshots/phone_portrait.png" width="200"/> | <img src="screenshots/phone_landscape.png" width="300"/> |
| **Tablet** | <img src="screenshots/tablet_portrait.png" width="250"/> | <img src="screenshots/tablet_landscape.png" width="350"/> |
| **Android TV / FireTV** | N/A (TV Mode) | <img src="screenshots/android_tv_landscape.png" width="400"/> |

### **UI States Demonstrated**
| State | Description | Screenshot |
|-------|-------------|------------|
| **Loading** | Data fetching in progress | <img src="screenshots/loading_state.png" width="200"/> |
| **Success** | Weather data displayed | <img src="screenshots/success_state.png" width="200"/> |
| **Error** | Network error with retry | <img src="screenshots/error_state.png" width="200"/> |
| **FireTV Mode** | Enhanced TV interface | <img src="screenshots/firetv_mode.png" width="300"/> |

### **Adaptive Features**
- **Automatic Orientation Detection**: Seamless transition between portrait/landscape layouts
- **Platform-Specific Sizing**: Larger fonts and touch targets for FireTV
- **Responsive Design**: Optimal layouts for different screen densities and sizes
- **Dark Theme Support**: FireTV automatically uses dark theme optimizations

*Note: To generate actual screenshots, run the app on different device emulators and capture the UI in various states.*

## 🧪 Testing Report

### **Test Coverage Summary**

| Module | Unit Tests | Integration Tests | UI Tests | **Total Tests** |
|--------|------------|-------------------|-----------|----------------|
| **app** | 7 (ViewModels) | 10 (Hilt DI) | 29 (Compose UI) | **46 tests** |
| **data** | 24 (Repository/Models) | 5 (API Integration) | N/A | **29 tests** |
| **domain** | 17 (Use Cases/Models) | 6 (Integration) | N/A | **23 tests** |
| **widget** | 48 (Components/Logic) | 7 (Error Handling) | 15 (Multi-Device) | **70 tests** |
| | | | | |
| **TOTAL PROJECT** | **96 Unit Tests** | **28 Integration Tests** | **44 UI Tests** | **🎯 168 TESTS** |

### **Detailed Test Breakdown**

#### **📱 App Module (46 tests)**
- **WeatherViewModelTest** (7 tests): ViewModel state management and error handling
- **MainActivityTest** (9 tests): Activity lifecycle and initialization
- **WeatherScreenTest** (11 tests): Compose screen UI behavior  
- **WeatherScreenIntegrationTest** (9 tests): End-to-end screen testing
- **HiltModuleTest** (10 tests): Dependency injection validation

#### **🌐 Data Module (29 tests)**
- **WeatherRepositoryImplTest** (9 tests): Repository logic and API integration
- **WeatherDataTest** (5 tests): Data model mapping and validation
- **WeatherResponseTest** (5 tests): API response parsing
- **ForecastResponseTest** (5 tests): Forecast data structure testing
- **WeatherIntegrationTest** (5 tests): Real API integration testing

#### **🏗️ Domain Module (23 tests)**
- **GetWeatherUseCaseTest** (5 tests): Weather retrieval business logic
- **GetTodayTemperatureUseCaseTest** (4 tests): Temperature use case validation
- **WeatherTest** (4 tests): Domain model functionality
- **ResultTest** (4 tests): Result wrapper behavior
- **UseCaseIntegrationTest** (6 tests): Cross-use case integration

#### **🎨 Widget Module (70 tests)**
- **WeatherWidgetLogicTest** (15 tests): Widget component logic
- **WeatherWidgetParameterTest** (11 tests): Parameter validation and edge cases
- **WeatherWidgetStateTest** (9 tests): State management in widgets
- **WeatherWidgetImageErrorHandlingUnitTest** (7 tests): Image error scenarios
- **WeatherWidgetImageErrorHandlingTest** (6 tests): Image fallback testing
- **WeatherWidgetTest** (15 tests): UI testing across devices
- **WeatherWidgetImageErrorIntegrationTest** (7 tests): Integration error testing

### **Test Categories by Type**

#### **🔧 Unit Tests (96 total)**
- **Business Logic**: Use cases, ViewModels, repositories
- **Data Models**: Domain and data layer model testing
- **Component Logic**: Widget behavior and state management

#### **🔗 Integration Tests (28 total)**  
- **API Integration**: Real network calls with mock servers
- **Module Integration**: Cross-module dependency testing
- **End-to-End Flows**: Complete user journey testing

#### **🎨 UI Tests (44 total)**
- **Compose Testing**: Widget rendering in different states
- **Multi-Device Testing**: Phone, tablet, and TV layouts
- **Error State Testing**: UI behavior during failures
- **Platform Testing**: Android vs FireTV UI differences

### **Testing Commands**

```bash
# Run all tests
./gradlew test

# Run specific module tests
./gradlew :app:test
./gradlew :data:test
./gradlew :domain:test
./gradlew :widget:test

# Run instrumented tests
./gradlew connectedAndroidTest

# Generate test reports
./gradlew testDebugUnitTest
```

## 🚀 Installation / Execution

### **Prerequisites**
- **Android Studio Hedgehog** (2023.1.1) or later
- **JDK 17** or higher
- **Android SDK** with API level 36
- **Git** for version control

### **Quick Start**

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/weather-challenge.git
   cd weather-challenge
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Build the Project**
   ```bash
   ./gradlew build
   ```

4. **Run the Application**
   ```bash
   # Google Play variant
   ./gradlew :app:installGoogleDebug
   
   # Amazon Appstore variant  
   ./gradlew :app:installAmazonDebug
   ```

### **Build Variants**

The project includes multiple build variants for different platforms:

```bash
# Google Android (Play Store)
./gradlew assembleGoogleDebug
./gradlew assembleGoogleRelease

# Amazon FireTV (Appstore)
./gradlew assembleAmazonDebug
./gradlew assembleAmazonRelease
```

### **Development Setup**

1. **API Configuration**
   - The app uses the free National Weather Service API
   - No API key required
   - Default location: San Jose, CA (37.2883, -121.8434)

2. **Code Quality**
   ```bash
   # Run code formatting
   ./gradlew ktlintFormat
   
   # Check code style
   ./gradlew ktlintCheck
   ```

3. **Testing Setup**
   ```bash
   # Install test dependencies
   ./gradlew build
   
   # Run all tests
   ./gradlew test connectedAndroidTest
   ```

### **Device Testing**

#### **Phone/Tablet Emulators**
- Pixel 4 (Phone)
- Pixel C (Tablet)
- Various API levels (24+)

#### **Android TV Emulators**
- Android TV (1080p)
- Fire TV (1080p/4K)

### **Production Deployment**

1. **Generate Signed APK**
   ```bash
   ./gradlew assembleRelease
   ```

2. **Google Play Store**
   - Use `googleRelease` variant
   - Follow Google Play Console guidelines

3. **Amazon Appstore**
   - Use `amazonRelease` variant
   - Follow Amazon Developer Console requirements

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

**Built with ❤️ using Modern Android Development practices**