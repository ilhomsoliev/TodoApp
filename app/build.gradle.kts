plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
//    id("com.google.devtools.ksp") version "2.0.0-1.0.21"
}

android {
    namespace = "com.ilhomsoliev.todo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ilhomsoliev.todo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.coil.compose)
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation(libs.androidx.constraintlayout.compose)
    // Reflection
    implementation(libs.kotlin.reflect)
    // ktor
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.core)
    // hilt
    implementation("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-compiler:2.49")
//    ksp("com.google.dagger:hilt-compiler:2.49")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // Lifecycle extensions
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.cardview)
    debugImplementation(libs.androidx.ui.tooling)
}