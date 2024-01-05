plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.m4ykey.navigation"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation(project(":album:ui"))

    libs.apply {
        implementation(androidx.core)
        implementation(androidx.appcompat)
        implementation(android.material)
        testImplementation(junit)
        androidTestImplementation(ext.junit)
        androidTestImplementation(espresso)

        implementation(androidx.navigation.ui)
        implementation(androidx.navigation.fragment)
    }

}