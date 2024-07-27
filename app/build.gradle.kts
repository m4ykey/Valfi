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

val versionMajor = 0
val versionMinor = 7
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
            isDebuggable = false
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
}

dependencies {

    implementation(project(":core"))
    implementation(project(":album:ui"))
    implementation(project(":news:ui"))
    implementation(project(":album:data"))
    implementation(project(":news:data"))
    implementation(project(":settings"))

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