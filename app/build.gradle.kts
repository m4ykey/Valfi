plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.gms)
    alias(libs.plugins.ksp)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.performance)
}

val versionMajor = 0
val versionMinor = 5
val versionPatch = 3

android {
    namespace = "com.m4ykey.valfi2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.m4ykey.valfi2"
        minSdk = 26
        targetSdk = 34
        versionCode = versionMajor * 1000000 + versionMinor * 10000 + versionPatch * 100
        versionName = "$versionMajor.$versionMinor.$versionPatch"

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
    implementation(project(":album:ui"))
    implementation(project(":news:ui"))
    implementation(project(":album:data"))
    implementation(project(":news:data"))

    libs.apply {

        implementation(androidxAppCompat)
        implementation(androidMaterial)
        implementation(androidxConstraintLayout)
        implementation(androidxCore)
        implementation(androidxNavigationUI)
        implementation(androidxNavigationFragment)

        testImplementation(junit)
        androidTestImplementation(extJunit)
        androidTestImplementation(espresso)

        implementation(hiltAndroid)
        ksp(hiltCompiler)

        implementation(timber)

        implementation(firebaseCrashlytics)
        implementation(firebasePerformance)
    }
}