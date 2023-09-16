plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.m4ykey.repository"
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

    implementation(project(":data:local"))
    implementation(project(":data:remote"))
    implementation(project(":core:common"))

    implementation(DaggerHilt.android)
    ksp(DaggerHilt.compiler)

    implementation(Network.Retrofit.retrofit)

    implementation(AndroidX.Core.core)
    implementation(AndroidX.Core.appCompat)
    implementation(AndroidX.Core.material)

    testImplementation(Test.TestImplementation.junit)
    implementation(Test.Testing.coreTesting)

    androidTestImplementation(Test.AndroidTestImplementation.extJUnit)
    androidTestImplementation(Test.AndroidTestImplementation.espresso)
}