import java.util.Properties

plugins {
    id("com.android.library")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.m4ykey.remote"
    compileSdk = Configuration.compileSdk

    defaultConfig {
        minSdk = Configuration.minSdk

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
        sourceCompatibility =  JavaVersion.VERSION_17
        targetCompatibility =  JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget =  JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(AndroidX.Core.core)
    implementation(AndroidX.Core.appCompat)
    implementation(AndroidX.Core.material)

    testImplementation(Test.TestImplementation.junit)
    implementation(Test.Testing.coreTesting)

    androidTestImplementation(Test.AndroidTestImplementation.extJUnit)
    androidTestImplementation(Test.AndroidTestImplementation.espresso)

    implementation(Network.Retrofit.retrofit)
    implementation(Network.Retrofit.okhttp)
    implementation(Network.Retrofit.loggingInterceptor)
    implementation(Network.Retrofit.moshi)
    implementation(Network.Retrofit.moshiKotlin)
    implementation(Network.Retrofit.moshiConverter)
    ksp(Network.Retrofit.moshiCodegen)

    implementation(DaggerHilt.android)
    ksp(DaggerHilt.compiler)

    implementation(AndroidX.DataStore.dataStore)
    implementation(AndroidX.DataStore.dataStorePreferences)

}