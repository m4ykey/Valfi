import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.m4ykey.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    val apiKey by lazy {
        rootProject.file("local.properties").inputStream().use { input ->
            Properties().apply { load(input) }
        }
    }

    buildTypes.all {
        buildConfigField("String", "NEWS_API_KEY", "\"${apiKey.getProperty("NEWS_API_KEY")}\"")
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
        buildConfig = true
    }
}

dependencies {

    implementation(project(":core"))

    libs.apply {

        implementation(retrofit)
        implementation(converterMoshi)

        testImplementation(junit)
        androidTestImplementation(extJunit)
        androidTestImplementation(espresso)

        implementation(coroutines)

        implementation(hiltAndroid)
        ksp(hiltCompiler)

        implementation(androidxPaging)

        implementation(bundles.okhttp)

    }
}