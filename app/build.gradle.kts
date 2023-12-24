import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.m4ykey.valfi2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.m4ykey.valfi2"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes.all {
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        buildConfigField("String", "SPOTIFY_CLIENT_ID", "\"${properties.getProperty("SPOTIFY_CLIENT_ID")}\"")
        buildConfigField("String", "SPOTIFY_CLIENT_SECRET", "\"${properties.getProperty("SPOTIFY_CLIENT_SECRET")}\"")
        buildConfigField("String", "TMDB_API_KEY", "\"${properties.getProperty("TMDB_API_KEY")}\"")
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core)

    implementation(libs.bundles.navigation)

    implementation(libs.bundles.firebase)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso)

    implementation(libs.coil)

    implementation(libs.timber)

}