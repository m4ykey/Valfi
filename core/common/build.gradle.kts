plugins {
    id("com.android.library")
    kotlin("kapt")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.m4ykey.common"
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

    implementation(AndroidX.core)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.material)

    testImplementation(TestImplementation.junit)
    implementation(Testing.coreTesting)

    androidTestImplementation(AndroidTestImplementation.extJUnit)
    androidTestImplementation(AndroidTestImplementation.espresso)

    implementation(Room.ktx)
    implementation(Room.runtime)
    annotationProcessor(Room.compiler)

}