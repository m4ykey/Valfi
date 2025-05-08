plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.m4ykey.authentication"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minSdkVersion"] as Int

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

    implementation(libs.bundles.okhttp)

    implementation(libs.retrofit)

    implementation(libs.bundles.datastore)

    implementation(libs.moshiKotlin)

    implementation(libs.bundles.coroutines)

    implementation(libs.androidxRoomRuntime)
    implementation(libs.androidxRoomKtx)
    ksp(libs.androidxRoomCompiler)

    implementation(libs.hiltAndroid)
    implementation(libs.androidxAppCompat)
    implementation(libs.androidMaterial)
    implementation(libs.androidxActivity)
    implementation(libs.androidxConstraintLayout)
    ksp(libs.hiltCompiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.extJunit)
    androidTestImplementation(libs.espresso)
}