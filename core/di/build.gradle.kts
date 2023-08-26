plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.m4ykey.di"
    compileSdk = 33

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(project(":data:local"))
    implementation(project(":data:remote"))
    implementation(project(":core:common"))
    implementation(project(":repository"))

    implementation(AndroidX.core)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.material)

    testImplementation(TestImplementation.junit)
    implementation(Testing.coreTesting)

    androidTestImplementation(AndroidTestImplementation.extJUnit)
    androidTestImplementation(AndroidTestImplementation.espresso)

    implementation(DaggerHilt.android)
    kapt(DaggerHilt.compiler)

    implementation(DataStore.dataStore)
    implementation(DataStore.dataStorePreferences)

    implementation(Retrofit.retrofit)
    implementation(Retrofit.gsonConverter)

    implementation(Room.ktx)
    implementation(Room.runtime)
    annotationProcessor(Room.compiler)

}