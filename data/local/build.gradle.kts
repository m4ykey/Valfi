plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
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
        jvmTarget = "17"
    }
}

dependencies {

    implementation(AndroidX.Core.core)
    implementation(AndroidX.Core.appCompat)
    implementation(AndroidX.Core.material)

    testImplementation(Test.TestImplementation.junit)
    implementation(Test.Testing.coreTesting)

    androidTestImplementation(Test.AndroidTestImplementation.extJUnit)
    androidTestImplementation(Test.AndroidTestImplementation.espresso)

    implementation(AndroidX.Room.runtime)
    implementation(AndroidX.Room.ktx)
    ksp(AndroidX.Room.compiler)

    implementation(DaggerHilt.android)
    ksp(DaggerHilt.compiler)

    implementation(Network.Retrofit.moshi)
    implementation(Network.Retrofit.moshiKotlin)

}