object AndroidX {

    object Compose {
        const val composeBom = "androidx.compose:compose-bom:2023.08.00"
        const val composeMaterial = "androidx.compose.material3:material3"
        const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
        const val composeUiTooling = "androidx.compose.ui:ui-tooling"
        const val composeUi = "androidx.compose.ui:ui"
        const val composeIcons = "androidx.compose.material:material-icons-extended"
        const val composeActivity = "androidx.activity:activity-compose"
        const val composeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose"
        const val composeNavigation = "androidx.navigation:navigation-compose"
    }

    object Core {
        const val core = "androidx.core:core-ktx:1.10.1"
        const val appCompat = "androidx.appcompat:appcompat:1.6.1"
        const val material = "com.google.android.material:material:1.9.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
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
}

object Coil {
    const val coil = "io.coil-kt:coil:${Version.coil}"
    const val coilCompose = "io.coil-kt:coil-compose:${Version.coil}"
}

object Firebase {
    const val firebaseBom = "com.google.firebase:firebase-bom:32.2.3"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
    const val firebasePerf = "com.google.firebase:firebase-perf-ktx"
    const val firebaseAuth = "com.google.firebase:firebase-auth-ktx"
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

object Test {
    object TestImplementation {
        const val junit = "junit:junit:4.13.2"
    }

    object AndroidTestImplementation {
        const val extJUnit = "androidx.test.ext:junit:1.1.5"
        const val espresso = "androidx.test.espresso:espresso-core:3.5.1"
    }

    object Testing {
        const val truth = "com.google.truth:truth:1.1.5"
        const val coreTesting = "androidx.arch.core:core-testing:2.2.0"
    }
}

object Network {
    object Retrofit {
        const val retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
        const val okhttp = "com.squareup.okhttp3:okhttp:${Version.okhttp}"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Version.okhttp}"
        const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:1.14.0"
        const val moshi = "com.squareup.moshi:moshi:1.14.0"
        const val moshiConverter = "com.squareup.retrofit2:converter-moshi:2.9.0"
    }
}