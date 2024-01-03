package com.m4ykey.valfi2.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.m4ykey.valfi2.album.data.remote.api.AlbumApi
import com.m4ykey.valfi2.album.data.remote.api.AuthApi
import com.m4ykey.core.network.createMoshi
import com.m4ykey.valfi2.album.data.domain.repository.AlbumRepository
import com.m4ykey.valfi2.album.data.remote.interceptor.SpotifyInterceptor
import com.m4ykey.valfi2.album.data.repository.AlbumRepositoryImpl
import com.m4ykey.valfi2.album.ui.AlbumViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single<AuthApi> { provideAuth(get()) }
    single<AlbumApi> { provideAlbum(get()) }
    single { createMoshi() }
}

val viewModelModule = module {
    viewModel { AlbumViewModel(get()) }
}

val repositoryModule = module {
    single<AlbumRepository> { AlbumRepositoryImpl(get(), get()) }
}

val interceptorModule = module {
    single<SpotifyInterceptor> { SpotifyInterceptor(get(), get()) }
}

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "spotify_key")

val dataStore = module {
    single {
        val context : Context = androidContext()
        context.dataStore
    }
}