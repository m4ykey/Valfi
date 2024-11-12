plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.m4ykey.data"
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

    implementation(libs.androidxCore)
    implementation(libs.androidxAppCompat)
    implementation(libs.androidMaterial)

    implementation(libs.retrofit)
    implementation(libs.converterMoshi)
    implementation(libs.moshiKotlin)
    implementation(libs.okhttp)
    implementation(libs.okhttpLoggingInterceptor)

    ksp(libs.hiltCompiler)
    implementation(libs.hiltAndroid)

    testImplementation(libs.junit)
    androidTestImplementation(libs.extJunit)
    androidTestImplementation(libs.espresso)
}