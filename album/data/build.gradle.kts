import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.m4ykey.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    configurations.all {
        exclude(group = "com.google.guava", module = "listenablefuture")
    }

    val apiKeys by lazy {
        rootProject.file("local.properties").inputStream().use { input ->
            Properties().apply { load(input) }
        }
    }

    buildTypes.all {
        buildConfigField("String", "SPOTIFY_CLIENT_ID", "\"${apiKeys.getProperty("SPOTIFY_CLIENT_ID")}\"")
        buildConfigField("String", "SPOTIFY_CLIENT_SECRET", "\"${apiKeys.getProperty("SPOTIFY_CLIENT_SECRET")}\"")
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
        testImplementation(junit)
        androidTestImplementation(ext.junit)
        androidTestImplementation(espresso)
        androidTestImplementation(truth)

        implementation(retrofit)
        implementation(converter.moshi)

        implementation(coroutines)

        implementation(hilt.android)
        ksp(hilt.compiler)

        implementation(androidx.paging)
        implementation(androidx.datastore)
        implementation(androidx.room.runtime)
        implementation(androidx.room.ktx)
        ksp(androidx.room.compiler)
        implementation(androidx.room.paging)

        implementation(bundles.okhttp)

    }
}