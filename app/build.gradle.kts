import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.m4ykey.valfi2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.m4ykey.valfi2"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes.all {
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        buildConfigField("String", "SPOTIFY_CLIENT_ID", "\"${properties.getProperty("SPOTIFY_CLIENT_ID")}\"")
        buildConfigField("String", "SPOTIFY_CLIENT_SECRET", "\"${properties.getProperty("SPOTIFY_CLIENT_SECRET")}\"")
        buildConfigField("String", "TMDB_API_KEY", "\"${properties.getProperty("TMDB_API_KEY")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
        buildConfig = true
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":navigation"))
    implementation(project(":album:ui"))

    libs.apply {

        implementation(androidx.appcompat)
        implementation(android.material)
        implementation(androidx.constraintlayout)
        implementation(androidx.core)

        implementation(firebase.crashlytics)

        testImplementation(junit)
        androidTestImplementation(ext.junit)
        androidTestImplementation(espresso)

        implementation(coil)

        implementation(hilt.android)
        ksp(hilt.compiler)

        implementation(androidx.navigation.fragment)
        implementation(androidx.navigation.ui)

    }
}