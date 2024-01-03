package com.m4ykey.valfi2

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.google.android.material.color.DynamicColors
import com.m4ykey.core.timberSetup
import com.m4ykey.valfi2.module.dataStore
import com.m4ykey.valfi2.module.interceptorModule
import com.m4ykey.valfi2.module.networkModule
import com.m4ykey.valfi2.module.repositoryModule
import com.m4ykey.valfi2.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApp : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)

        timberSetup()

        startKoin {
            androidContext(this@BaseApp)
            modules(listOf(networkModule, viewModelModule, repositoryModule, interceptorModule, dataStore))
        }
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