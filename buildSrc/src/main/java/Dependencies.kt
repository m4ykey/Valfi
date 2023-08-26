object Version {
    const val core = "1.10.1"
    const val appcompat = "1.6.1"
    const val material = "1.9.0"
    const val constraintlayout = "2.1.4"
    const val junit = "4.13.2"
    const val truth = "1.1.5"
    const val coroutines = "1.7.3"
    const val coreTesting = "2.2.0"
    const val extJunit = "1.1.5"
    const val espresso = "3.5.1"
    const val retrofit = "2.9.0"
    const val lifecycle = "2.6.1"
    const val hilt = "2.47"
    const val navigation = "2.7.1"
    const val okhttp = "5.0.0-alpha.11"
    const val room = "2.5.2"
    const val firebaseBom = "32.2.3"
    const val gson = "2.10.1"
    const val coil = "2.4.0"
    const val dataStore = "1.0.0"
    const val composeBom = "2023.08.00"
}

object Compose {
    const val composeBom = "androidx.compose:compose-bom:${Version.composeBom}"
    const val composeMaterial = "androidx.compose.material3:material3"
    const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling"
    const val composeUi = "androidx.compose.ui:ui"
    const val composeIcons = "androidx.compose.material:material-icons-extended"
    const val composeActivity = "androidx.activity:activity-compose"
    const val composeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose"
    const val composeNavigation = "androidx.navigation:navigation-compose"
}

object Coil {
    const val coil = "io.coil-kt:coil:${Version.coil}"
    const val coilCompose = "io.coil-kt:coil-compose:${Version.coil}"
}

object Firebase {
    const val firebaseBom = "com.google.firebase:firebase-bom:${Version.firebaseBom}"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
    const val firebasePerf = "com.google.firebase:firebase-perf-ktx"
}

object Retrofit {
    const val retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
    const val gsonConverter = "com.squareup.retrofit2:converter-gson:${Version.retrofit}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Version.okhttp}"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Version.okhttp}"
}

object AndroidX {
    const val core = "androidx.core:core-ktx:${Version.core}"
    const val appCompat = "androidx.appcompat:appcompat:${Version.appcompat}"
    const val material = "com.google.android.material:material:${Version.material}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Version.constraintlayout}"
}

object DaggerHilt {
    const val compiler = "com.google.dagger:hilt-compiler:${Version.hilt}"
    const val android = "com.google.dagger:hilt-android:${Version.hilt}"
}

object Coroutines {
    const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.coroutines}"
    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"
    const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"
}

object TestImplementation {
    const val junit = "junit:junit:${Version.junit}"
}

object AndroidTestImplementation {
    const val extJUnit = "androidx.test.ext:junit:${Version.extJunit}"
    const val espresso = "androidx.test.espresso:espresso-core:${Version.espresso}"
}

object Testing {
    const val truth = "com.google.truth:truth:${Version.truth}"
    const val coreTesting = "androidx.arch.core:core-testing:${Version.coreTesting}"
}

object Gson {
    const val gson = "com.google.code.gson:gson:${Version.gson}"
}

object DataStore {
    const val dataStore = "androidx.datastore:datastore:${Version.dataStore}"
    const val dataStorePreferences = "androidx.datastore:datastore-preferences:${Version.dataStore}"
}

object Room {
    const val compiler = "androidx.room:room-compiler:${Version.room}"
    const val runtime = "androidx.room:room-runtime:${Version.room}"
    const val ktx = "androidx.room:room-ktx:${Version.room}"
}

object Navigation {
    const val fragment = "androidx.navigation:navigation-fragment-ktx:${Version.navigation}"
    const val ui = "androidx.navigation:navigation-ui-ktx:${Version.navigation}"
}

object Lifecycle {
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycle}"
    const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycle}"
}