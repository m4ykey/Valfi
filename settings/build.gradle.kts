import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.safeargs)
}

val versionProperties = Properties().apply {
    load(rootProject.file("version.properties").inputStream())
}

val versionMajor = versionProperties["versionMajor"].toString().toInt()
val versionMinor = versionProperties["versionMinor"].toString().toInt()
val versionPatch = versionProperties["versionPatch"].toString().toInt()

android {
    namespace = "com.m4ykey.settings"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minSdkVersion"] as Int

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "APP_VERSION", "\"$versionMajor.$versionMinor.$versionPatch\"")
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

    implementation(libs.androidxActivity)
    implementation(libs.androidxConstraintLayout)

    testImplementation(libs.junit)

    androidTestImplementation(libs.extJunit)
    androidTestImplementation(libs.espresso)

    implementation(libs.gson)

    ksp(libs.hiltCompiler)
    implementation(libs.hiltAndroid)
}