package com.m4ykey.ui.navigation

import com.m4ykey.core.Navigator

interface AlbumNavigator : Navigator {

    fun navigateToAlbumSearch()
    fun navigateToAlbumDetail(albumId: String)
    fun navigateToStatistics()
    fun navigateToListenLater()
    fun navigateToNewRelease()
    fun navigateToAlbumCover(image : String)

}