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
    implementation(project(":artist:data"))

    implementation(libs.androidxCore)
    implementation(libs.androidxAppCompat)
    implementation(libs.androidMaterial)
    implementation(libs.androidxActivity)
    implementation(libs.androidxConstraintLayout)
    implementation(libs.androidxLifecycleRuntime)
    implementation(libs.androidxLifecycleViewmodel)
    implementation(libs.androidxRecyclerView)
    implementation(libs.androidxNavigationFragment)
    implementation(libs.androidxNavigationUI)

    implementation(libs.coroutines)

    ksp(libs.hiltCompiler)
    implementation(libs.hiltAndroid)

    implementation(libs.glide)

    testImplementation(libs.junit)

    androidTestImplementation(libs.extJunit)
    androidTestImplementation(libs.espresso)
}