plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
}

android {
    namespace = "com.example.vuey"
    compileSdk = Configuration.compileSdk

    defaultConfig {
        applicationId = Configuration.applicationId
        minSdk = Configuration.minSdk
        targetSdk = Configuration.targetSdk
        versionCode = Configuration.versionCode
        versionName = Configuration.versionName

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
        sourceCompatibility = Configuration.javaVersion
        targetCompatibility = Configuration.javaVersion
    }
    kotlinOptions {
        jvmTarget = Configuration.javaVersion.toString()
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":data:local"))
    implementation(project(":data:remote"))
    implementation(project(":repository"))

    implementation(AndroidX.core)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.material)
    implementation(AndroidX.constraintLayout)

    testImplementation(TestImplementation.junit)
    implementation(Testing.truth)
    implementation(Coroutines.test)
    implementation(Testing.coreTesting)

    androidTestImplementation(AndroidTestImplementation.extJUnit)
    androidTestImplementation(AndroidTestImplementation.espresso)

    // Kotlin Lifecycle
    implementation(Lifecycle.viewModel)
    implementation(Lifecycle.runtime)

    // Room
    implementation(Room.ktx)

    // Kotlin Coroutines
    implementation(Coroutines.core)
    implementation(Coroutines.android)

    // Dagger - Hilt
    kapt(DaggerHilt.compiler)
    implementation(DaggerHilt.android)

    // Navigation
    implementation(Navigation.fragment)
    implementation(Navigation.ui)

    // Firebase
    implementation(platform(Firebase.firebaseBom))
    implementation(Firebase.firebaseAnalytics)
    implementation(Firebase.firebaseCrashlytics)
    implementation(Firebase.firebasePerf)

    // Coil
    implementation(Coil.coil)
    implementation(Coil.coilCompose)

    // Jetpack Compose
    implementation(platform(Compose.composeBom))
    implementation(Compose.composeMaterial)
    implementation(Compose.composeUiToolingPreview)
    debugImplementation(Compose.composeUiTooling)
    implementation(Compose.composeUi)
    implementation(Compose.composeIcons)
    implementation(Compose.composeActivity)
    implementation(Compose.composeViewModel)
    implementation(Compose.composeNavigation)

}