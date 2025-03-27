package com.m4ykey.ui.navigation

import androidx.navigation.NavController
import com.m4ykey.album.ui.R
import com.m4ykey.ui.album.AlbumDetailFragmentDirections
import com.m4ykey.ui.album.AlbumHomeFragmentDirections
import com.m4ykey.ui.album.AlbumListenLaterFragmentDirections
import com.m4ykey.ui.album.AlbumNewReleaseFragmentDirections
import com.m4ykey.ui.album.AlbumSearchFragmentDirections

class AlbumNavigatorImpl(
    private val navController : NavController
) : AlbumNavigator {

    override fun navigateToAlbumDetail(albumId: String) {
        when (navController.currentDestination?.id) {
            R.id.albumHomeFragment -> {
                navController.navigate(
                    AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumDetailFragment(albumId)
                )
            }
            R.id.albumListenLaterFragment -> {
                navController.navigate(
                    AlbumListenLaterFragmentDirections.actionAlbumListenLaterFragmentToAlbumDetailFragment(albumId)
                )
            }
            R.id.albumNewReleaseFragment -> {
                navController.navigate(
                    AlbumNewReleaseFragmentDirections.actionAlbumNewReleaseFragmentToAlbumDetailFragment(albumId)
                )
            }
            R.id.albumSearchFragment -> {
                navController.navigate(
                    AlbumSearchFragmentDirections.actionAlbumSearchFragmentToAlbumDetailFragment(albumId)
                )
            }
        }
    }

    override fun navigateToAlbumSearch() {
        when (navController.currentDestination?.id) {
            R.id.albumHomeFragment -> {
                navController.navigate(
                    AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumSearchFragment()
                )
            }
            R.id.albumListenLaterFragment -> {
                navController.navigate(
                    AlbumListenLaterFragmentDirections.actionAlbumListenLaterFragmentToAlbumSearchFragment()
                )
            }
        }
    }

    override fun navigateToStatistics() {
        navController.navigate(
            AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumStatisticsFragment()
        )
    }

    override fun navigateToListenLater() {
        navController.navigate(
            AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumListenLaterFragment()
        )
    }

    override fun navigateToNewRelease() {
        navController.navigate(
            AlbumHomeFragmentDirections.actionAlbumHomeFragmentToAlbumNewReleaseFragment()
        )
    }

    override fun navigateToAlbumCover(image : String) {
        navController.navigate(
            AlbumDetailFragmentDirections.actionAlbumDetailFragmentToAlbumCoverFragment(image)
        )
    }
}