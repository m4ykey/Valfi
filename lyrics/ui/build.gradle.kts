plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.safeargs)
}

android {
    namespace = "com.lyrics.ui"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":lyrics:data"))

    implementation(libs.androidxCore)
    implementation(libs.androidxAppCompat)
    implementation(libs.androidMaterial)
    implementation(libs.androidxActivity)
    implementation(libs.androidxConstraintLayout)
    implementation(libs.androidxNavigationFragment)
    implementation(libs.androidxNavigationUI)
    implementation(libs.androidxLifecycleRuntime)
    implementation(libs.androidxLifecycleViewmodel)


    testImplementation(libs.junit)
    androidTestImplementation(libs.extJunit)
    androidTestImplementation(libs.espresso)

    ksp(libs.hiltCompiler)
    implementation(libs.hiltAndroid)

    implementation(libs.coroutines)
}