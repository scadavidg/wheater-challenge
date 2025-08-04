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
<img width="1546" height="931" alt="image" src="" />

## 📸 Demo - Multi-Device Screenshots
| Device Type | Portrait | Landscape |
|------------|----------|-----------|
| **Phone (Medium)** | <img src="https://github.com/user-attachments/assets/f3378e7c-a6fd-4c37-8daa-f7dde55ec855" width="200"/> | <img src="https://github.com/user-attachments/assets/0903e579-b137-46ab-a295-2b49b3278a38" width="300"/> |
| **Tablet** | <img src="https://github.com/user-attachments/assets/83847e10-40fa-46ad-8429-2c0174008d6a" width="250"/> | <img src="https://github.com/user-attachments/assets/00b196f2-e200-4197-a1df-596a61db2e6d" width="350"/> |
| **Android TV / FireTV** | N/A (TV Mode) | <img src="https://github.com/user-attachments/assets/4b69c700-2c29-40ba-8030-967bdf38d5a7" width="400"/> |

### **Adaptive Features**
- **Automatic Orientation Detection**: Seamless transition between portrait/landscape layouts
- **Platform-Specific Sizing**: Larger fonts and touch targets for FireTV
- **Responsive Design**: Optimal layouts for different screen densities and sizes
- **Dark Theme Support**: FireTV automatically uses dark theme optimizations

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
---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Built with ❤️ using Modern Android Development practices**
