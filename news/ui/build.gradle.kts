plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.safeargs)
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
    implementation(project(":news:data"))

    libs.apply {
        implementation(androidxCore)
        implementation(androidxAppCompat)
        implementation(androidxConstraintLayout)
        implementation(androidxPaging)
        implementation(androidxBrowser)

        implementation(androidMaterial)

        testImplementation(junit)

        androidTestImplementation(extJunit)
        androidTestImplementation(espresso)

        implementation(bundles.navigation)
        implementation(bundles.lifecycle)

        ksp(hiltCompiler)
        implementation(hiltAndroid)

    }
}