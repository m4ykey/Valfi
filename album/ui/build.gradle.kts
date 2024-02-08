plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.safeargs)
    alias(libs.plugins.hilt)
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

    libs.apply {
        implementation(androidx.core)
        implementation(androidx.appcompat)
        implementation(androidx.palette)
        implementation(androidx.constraintlayout)
        implementation(android.material)
        implementation(androidx.paging)

        testImplementation(junit)

        androidTestImplementation(ext.junit)
        androidTestImplementation(espresso)

        implementation(bundles.navigation)
        implementation(bundles.lifecycle)

        ksp(hilt.compiler)
        implementation(hilt.android)

        implementation(coil)
    }
}