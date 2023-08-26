import java.util.Properties

plugins {
    id("com.android.library")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.m4ykey.remote"
    compileSdk = 33

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        val properties = Properties()
        properties.load(rootProject.file("local.properties").inputStream())

        buildConfigField("String", "TMDB_API_KEY", "\"${properties.getProperty("TMDB_API_KEY")}\"")
        buildConfigField("String", "SPOTIFY_CLIENT_ID", "\"${properties.getProperty("SPOTIFY_CLIENT_ID")}\"")
        buildConfigField("String", "SPOTIFY_CLIENT_SECRET", "\"${properties.getProperty("SPOTIFY_CLIENT_SECRET")}\"")

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
        buildConfig = true
    }
}

dependencies {

    implementation(AndroidX.core)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.material)

    testImplementation(TestImplementation.junit)
    implementation(Testing.coreTesting)

    androidTestImplementation(AndroidTestImplementation.extJUnit)
    androidTestImplementation(AndroidTestImplementation.espresso)

    implementation(Retrofit.retrofit)
    implementation(Retrofit.gsonConverter)
    implementation(Gson.gson)

    implementation(Retrofit.okhttp)
    implementation(Retrofit.loggingInterceptor)

    implementation(DaggerHilt.android)
    kapt(DaggerHilt.compiler)

    implementation(DataStore.dataStore)
    implementation(DataStore.dataStorePreferences)

}