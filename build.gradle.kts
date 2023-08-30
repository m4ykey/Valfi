// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.1")
        classpath("com.google.gms:google-services:4.3.15")
        classpath("com.android.tools.build:gradle:8.1.1")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
        classpath("com.google.firebase:perf-plugin:1.4.2")
    }
}
plugins {
    id ("com.android.application") version "8.1.1" apply false
    id ("com.android.library") version "8.1.1" apply false
    id ("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id ("com.google.dagger.hilt.android") version "2.47" apply false
    id ("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"
}