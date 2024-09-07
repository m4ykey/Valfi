plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.safeargs)
}

android {
    namespace = "com.m4ykey.settings"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":album:data"))

    implementation(libs.androidxCore)
    implementation(libs.androidxAppCompat)
    implementation(libs.androidMaterial)
    implementation(libs.androidxNavigationUI)
    implementation(libs.androidxNavigationFragment)
    implementation(libs.androidxLifecycleViewmodel)
    implementation(libs.androidxLifecycleRuntime)
    implementation(libs.androidxDatastore)

    implementation(libs.mpAndroidChart)

    implementation(libs.activity)
    implementation(libs.androidxConstraintLayout)

    testImplementation(libs.junit)

    androidTestImplementation(libs.extJunit)
    androidTestImplementation(libs.espresso)

    ksp(libs.hiltCompiler)
    implementation(libs.hiltAndroid)
}