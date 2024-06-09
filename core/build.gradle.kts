import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.m4ykey.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val apiKeys by lazy {
        rootProject.file("local.properties").inputStream().use { input ->
            Properties().apply { load(input) }
        }
    }

    buildTypes.all {
        buildConfigField("String", "SPOTIFY_CLIENT_ID", "\"${apiKeys.getProperty("SPOTIFY_CLIENT_ID")}\"")
        buildConfigField("String", "SPOTIFY_CLIENT_SECRET", "\"${apiKeys.getProperty("SPOTIFY_CLIENT_SECRET")}\"")
        buildConfigField("String", "NEWS_API_KEY", "\"${apiKeys.getProperty("NEWS_API_KEY")}\"")
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

    libs.apply {
        implementation(androidxAppCompat)
        implementation(androidxCore)
        implementation(androidxPalette)
        implementation(androidxDatastore)

        implementation(androidMaterial)

        implementation(moshiKotlin)
        implementation(converterMoshi)
        implementation(retrofit)
        implementation(okhttp)
        implementation(okhttpLoggingInterceptor)

        ksp(hiltCompiler)
        implementation(hiltAndroid)

        implementation(glide)

    }
}