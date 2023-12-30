package com.m4ykey.valfi2.module

import com.m4ykey.valfi2.album.data.remote.api.AlbumApi
import com.m4ykey.valfi2.album.data.remote.api.AuthApi
import com.m4ykey.core.network.createMoshi
import org.koin.dsl.module

val networkModule = module {
    single<AuthApi> { provideAuth(get()) }
    single<AlbumApi> { provideAlbum(get()) }
    single { createMoshi() }
}

val appModule = listOf(networkModule)