import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.gms)
    alias(libs.plugins.ksp)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.performance)
}

val versionProperties = Properties().apply {
    load(rootProject.file("version.properties").inputStream())
}

val versionMajor = versionProperties["versionMajor"].toString().toInt()
val versionMinor = versionProperties["versionMinor"].toString().toInt()
val versionPatch = versionProperties["versionPatch"].toString().toInt()

android {
    namespace = "com.m4ykey.valfi2"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int

    defaultConfig {
        applicationId = "com.m4ykey.valfi2"
        minSdk = rootProject.extra["minSdkVersion"] as Int
        targetSdk = rootProject.extra["targetSdkVersion"] as Int
        versionCode = versionMajor * 1000000 + versionMinor * 10000 + versionPatch * 100
        versionName = "$versionMajor.$versionMinor.$versionPatch"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        val keyStorePropertiesFile = rootProject.file("keystore.properties")
        val keyStoreProperties = Properties()
        keyStoreProperties.load(FileInputStream(keyStorePropertiesFile))

        create("release") {
            keyAlias = keyStoreProperties["keyAlias"] as String
            keyPassword = keyStoreProperties["keyPassword"] as String
            storePassword = keyStoreProperties["storePassword"] as String
            storeFile = file(keyStoreProperties["storeFile"] as String)
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ""
            versionNameSuffix = "-debug"
            isMinifyEnabled = false
            isDebuggable = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
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
    implementation(project(":album:data"))
    implementation(project(":settings:ui"))
    implementation(project(":settings:data"))
    implementation(project(":lyrics:ui"))
    implementation(project(":lyrics:data"))

    libs.apply {

        implementation(androidxAppCompat)
        implementation(androidMaterial)
        implementation(androidxConstraintLayout)
        implementation(androidxCore)
        implementation(androidxNavigationUI)
        implementation(androidxNavigationFragment)
        implementation(androidxDatastore)

        testImplementation(junit)
        androidTestImplementation(extJunit)
        androidTestImplementation(espresso)

        //debugImplementation(leakcanary)

        implementation(bundles.coroutines)

        implementation(hiltAndroid)
        ksp(hiltCompiler)

        implementation(firebaseCrashlytics)
        implementation(firebasePerformance)
        implementation(firebaseMessaging)

    }
}