plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.m4ykey.ui"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":navigation"))
    implementation(project(":album:data"))

    libs.apply {
        implementation(androidx.core)
        implementation(androidx.appcompat)

        testImplementation(junit)

        androidTestImplementation(ext.junit)
        androidTestImplementation(espresso)

        implementation(androidx.constraintlayout)
        implementation(android.material)

        implementation(androidx.navigation.fragment)

        implementation(androidx.lifecycle.runtime)
        implementation(androidx.lifecycle.viewmodel)

        implementation(androidx.paging)

        ksp(hilt.compiler)
        implementation(hilt.android)

        implementation(coil)

    }
}