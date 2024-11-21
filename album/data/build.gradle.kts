plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.m4ykey.album.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation(project(":core"))
    implementation(project(":authentication"))

    libs.apply {

        implementation(retrofit)
        implementation(converterMoshi)
        implementation(moshiKotlin)
        implementation(okhttp)
        implementation(okhttpLoggingInterceptor)

        implementation(coroutines)

        implementation(hiltAndroid)
        ksp(hiltCompiler)

        implementation(androidxRoomRuntime)
        implementation(androidxRoomKtx)
        ksp(androidxRoomCompiler)
        implementation(androidxDatastore)
    }
}