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
}

dependencies {

    libs.apply {
        implementation(androidxAppCompat)
        implementation(androidxCore)
        implementation(androidMaterial)

        implementation(moshiKotlin)
        implementation(converterMoshi)
        implementation(retrofit)

        ksp(hiltCompiler)
        implementation(hiltAndroid)

        implementation(androidxDatastore)

        implementation(okhttp)
        implementation(okhttpLoggingInterceptor)

        implementation(glide)

        implementation(androidxPalette)

    }
}