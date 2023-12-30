package com.m4ykey.valfi2.module

import com.m4ykey.valfi2.album.data.remote.api.AuthApi
import com.m4ykey.valfi2.core.network.createMoshi
import org.koin.dsl.module

val networkModule = module {
    single<AuthApi> { provideAuth(get()) }
    single { createMoshi() }
}

val appModule = listOf(networkModule)