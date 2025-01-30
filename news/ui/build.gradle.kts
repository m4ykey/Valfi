plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.safeargs)
}

android {
    namespace = "com.m4ykey.ui"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minSdkVersion"] as Int

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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":news:data"))

    libs.apply {
        implementation(androidxCore)
        implementation(androidxAppCompat)
        implementation(androidxConstraintLayout)
        implementation(androidxBrowser)
        implementation(androidxNavigationFragment)
        implementation(androidxNavigationUI)
        implementation(androidxLifecycleRuntime)
        implementation(androidxLifecycleViewmodel)

        implementation(androidMaterial)

        testImplementation(junit)

        androidTestImplementation(extJunit)
        androidTestImplementation(espresso)

        ksp(hiltCompiler)
        implementation(hiltAndroid)

    }
}