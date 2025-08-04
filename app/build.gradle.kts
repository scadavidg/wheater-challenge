plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.wheaterchallenge"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.wheaterchallenge"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    flavorDimensions += "platform"
    productFlavors {
        create("google") {
            dimension = "platform"
            applicationIdSuffix = ".google"
            versionNameSuffix = "-google"
        }
        create("amazon") {
            dimension = "platform"
            applicationIdSuffix = ".amazon"
            versionNameSuffix = "-amazon"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            // Enable JUnit 5 for unit tests
            all {
                it.useJUnitPlatform()
            }
        }
    }
}

dependencies {
    // ===== PROJECT MODULES =====
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":widget"))

    // ===== ANDROID CORE =====
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // ===== JETPACK COMPOSE =====
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // ===== DEPENDENCY INJECTION =====
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.hilt.compiler)

    // ===== UNIT TESTING =====
    // JUnit 5 (Modern Testing Framework)
    testImplementation(libs.junit5.api)
    testImplementation(libs.junit5.params)
    testRuntimeOnly(libs.junit5.engine)
    
    // JUnit 4 (Legacy Compatibility)
    testImplementation(libs.junit4)
    testRuntimeOnly(libs.junit5.vintage)
    
    // Testing Utilities
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.turbine) // Flow testing

    // ===== INSTRUMENTATION TESTING =====
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // ===== DEBUG TOOLS =====
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}