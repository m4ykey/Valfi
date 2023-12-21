package com.m4ykey.valfi2

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.google.android.material.color.DynamicColors
import com.m4ykey.valfi2.core.timberSetup

class BaseApp : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)

        timberSetup()
    }

    private val cachePolicy = CachePolicy.ENABLED

    override fun newImageLoader(): ImageLoader {
        return ImageLoader(this).newBuilder()
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.1)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(0.05)
                    .directory(cacheDir)
                    .build()
            }
            .diskCachePolicy(cachePolicy)
            .memoryCachePolicy(cachePolicy)
            .logger(DebugLogger())
            .build()
    }
}