plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.safeargs)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.m4ykey.album.ui"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":settings"))
    implementation(project(":album:data"))

    libs.apply {
        implementation(androidxCore)
        implementation(androidxAppCompat)
        implementation(androidxPalette)
        implementation(androidxConstraintLayout)
        implementation(androidMaterial)
        implementation(androidxNavigationFragment)
        implementation(androidxNavigationUI)
        implementation(androidxLifecycleRuntime)
        implementation(androidxLifecycleViewmodel)

        testImplementation(junit)

        androidTestImplementation(extJunit)
        androidTestImplementation(espresso)

        ksp(hiltCompiler)
        implementation(hiltAndroid)

        implementation(coroutines)

        implementation(glide)

    }
}