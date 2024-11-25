plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.musicproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.musicproject"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Constrain Layout

    implementation (libs.androidx.constraintlayout.compose)
    // Hilt implementation
    implementation(libs.hilt.android)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.runtime.saved.instance.state)
    implementation(libs.firebase.firestore.ktx)
    kapt(libs.hilt.android.compiler)

    // For Jetpack Compose integration:
    implementation(libs.androidx.hilt.navigation.compose)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // Mockito Kotlin
    testImplementation (libs.mockito.kotlin)
    androidTestImplementation( libs.mockito.kotlin)
    androidTestImplementation (libs.mockito.android)

    // Navigation Testing
        androidTestImplementation (libs.androidx.navigation.testing.v283)
    // retrofit
    implementation(libs.retrofit)
    //json converter
    implementation (libs.converter.gson.v2110)
    // json parser
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    // image loader
    implementation (libs.compose)
    // system ui controller
    implementation (libs.accompanist.systemuicontroller)
    // media player
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(kotlin("script-runtime"))
}