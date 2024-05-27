plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.safeargs)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
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
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":album:data"))

    libs.apply {
        implementation(androidxCore)
        implementation(androidxAppCompat)
        implementation(androidxPalette)
        implementation(androidxConstraintLayout)
        implementation(androidMaterial)
        implementation(androidxPaging)

        testImplementation(junit)

        androidTestImplementation(extJunit)
        androidTestImplementation(espresso)

        implementation(bundles.navigation)
        implementation(bundles.lifecycle)

        ksp(hiltCompiler)
        implementation(hiltAndroid)

        implementation(vicoViews)
        implementation(vicoCore)

        implementation(coroutines)

    }
}