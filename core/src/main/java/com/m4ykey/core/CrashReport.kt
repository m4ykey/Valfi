package com.m4ykey.core

import android.util.Log.ERROR
import com.google.firebase.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashReportTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val crashlytics = FirebaseCrashlytics.getInstance()

        crashlytics.log(message)

        if (priority == ERROR && t != null) {
            crashlytics.recordException(t)
        }
    }
}

fun timberSetup() {
    Timber.plant(if (BuildConfig.DEBUG) {
        object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String {
                return "${element.fileName}, ${element.lineNumber}, ${element.methodName}"
            }
        }
    } else {
        CrashReportTree()
    })
}