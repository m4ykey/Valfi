plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.gms)
}

val versionMajor = 0
val versionMinor = 3
val versionPatch = 11
val versionBuild = 1
var versionExt = ""

if (versionBuild > 0) {
    versionExt = ".${versionBuild}-beta"
}

android {
    namespace = "com.m4ykey.valfi2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.m4ykey.valfi2"
        minSdk = 26
        targetSdk = 34
        versionCode = versionMajor * 1000000 + versionMinor * 10000 + versionPatch * 100 + versionBuild
        versionName = "${versionMajor}.${versionMinor}.${versionPatch}${versionExt}"

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
        buildConfig = true
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":album:ui"))

    libs.apply {

        implementation(androidx.appcompat)
        implementation(android.material)
        implementation(androidx.constraintlayout)
        implementation(androidx.core)

        implementation(firebase.crashlytics)

        testImplementation(junit)
        androidTestImplementation(ext.junit)
        androidTestImplementation(espresso)

        implementation(hilt.android)
        ksp(hilt.compiler)

        implementation(libs.bundles.navigation)

    }
}