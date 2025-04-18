plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.m4ykey.core"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minSdkVersion"] as Int
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        //buildConfig = true
    }
}

dependencies {

    libs.apply {
        implementation(androidxAppCompat)
        implementation(androidxCore)
        implementation(androidxPalette)
        implementation(androidxDatastore)
        implementation(androidxBrowser)
        implementation(androidxLifecycleRuntime)
        implementation(androidxLifecycleViewmodel)

        implementation(androidMaterial)

        implementation(moshiKotlin)
        implementation(converterMoshi)
        implementation(retrofit)
        implementation(okhttp)
        implementation(okhttpLoggingInterceptor)
        ksp(moshiCodegen)

        ksp(hiltCompiler)
        implementation(hiltAndroid)

        implementation(glide)

        implementation(androidxPagingRuntime)

    }
}