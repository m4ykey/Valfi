package com.m4ykey.ui.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object AlbumNavigatorModule {

    @Provides
    @FragmentScoped
    fun provideAlbumNavigator(fragment: Fragment) : AlbumNavigator {
        val navController = NavHostFragment.findNavController(fragment)
        return AlbumNavigatorImpl(navController)
    }

}