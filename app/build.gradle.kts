plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("com.google.devtools.ksp")
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
        sourceCompatibility =  JavaVersion.VERSION_17
        targetCompatibility =  JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget =  JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":data:local"))
    implementation(project(":data:remote"))
    implementation(project(":repository"))

    implementation(AndroidX.Core.core)
    implementation(AndroidX.Core.appCompat)
    implementation(AndroidX.Core.material)
    implementation(AndroidX.Core.constraintLayout)

    testImplementation(Test.TestImplementation.junit)
    implementation(Test.Testing.truth)
    implementation(Coroutines.test)
    implementation(Test.Testing.coreTesting)

    androidTestImplementation(Test.AndroidTestImplementation.extJUnit)
    androidTestImplementation(Test.AndroidTestImplementation.espresso)

    // Kotlin Lifecycle
    implementation(AndroidX.Lifecycle.viewModel)
    implementation(AndroidX.Lifecycle.runtime)

    // Room
    implementation(AndroidX.Room.ktx)

    // Kotlin Coroutines
    implementation(Coroutines.core)
    implementation(Coroutines.android)

    // Dagger - Hilt
    ksp(DaggerHilt.compiler)
    implementation(DaggerHilt.android)

    // Navigation
    implementation(AndroidX.Navigation.fragment)
    implementation(AndroidX.Navigation.ui)

    // Firebase
    implementation(platform(Firebase.firebaseBom))
    implementation(Firebase.firebaseAnalytics)
    implementation(Firebase.firebaseCrashlytics)
    implementation(Firebase.firebasePerf)
    implementation(Firebase.firebaseAuth)

    // Coil
    implementation(Coil.coil)
    implementation(Coil.coilCompose)

    // Jetpack Compose
    implementation(platform(AndroidX.Compose.composeBom))
    implementation(AndroidX.Compose.composeMaterial)
    implementation(AndroidX.Compose.composeUiToolingPreview)
    debugImplementation(AndroidX.Compose.composeUiTooling)
    implementation(AndroidX.Compose.composeUi)
    implementation(AndroidX.Compose.composeIcons)
    implementation(AndroidX.Compose.composeActivity)
    implementation(AndroidX.Compose.composeViewModel)
    implementation(AndroidX.Compose.composeNavigation)

}