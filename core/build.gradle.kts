plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
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
        implementation(androidx.appcompat)
        implementation(android.material)
        implementation(androidx.core)

        testImplementation(junit)
        androidTestImplementation(ext.junit)
        androidTestImplementation(espresso)

        implementation(moshi.kotlin)
        implementation(converter.moshi)
        implementation(retrofit)

        implementation(timber)

        implementation(firebase.crashlytics)

        ksp(hilt.compiler)
        implementation(hilt.android)

        implementation(okhttp)
        implementation(okhttp.logging.interceptor)

        implementation(glide)

        implementation(androidx.palette)

    }
}