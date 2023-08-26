plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.m4ykey.local"
    compileSdk = Configuration.compileSdk

    defaultConfig {
        minSdk = Configuration.minSdk

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
        sourceCompatibility =  JavaVersion.VERSION_17
        targetCompatibility =  JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget =  JavaVersion.VERSION_17.toString()
    }
}

dependencies {

    implementation(AndroidX.core)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.material)

    testImplementation(TestImplementation.junit)
    implementation(Testing.coreTesting)

    androidTestImplementation(AndroidTestImplementation.extJUnit)
    androidTestImplementation(AndroidTestImplementation.espresso)

    implementation(Room.runtime)
    implementation(Room.ktx)
    ksp(Room.compiler)

    implementation(Gson.gson)

    implementation(DaggerHilt.android)
    kapt(DaggerHilt.compiler)

}