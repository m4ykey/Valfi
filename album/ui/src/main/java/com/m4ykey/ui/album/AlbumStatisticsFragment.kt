package com.m4ykey.ui.album

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.m4ykey.album.ui.R
import com.m4ykey.album.ui.databinding.FragmentAlbumStatisticsBinding
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.loadImage
import com.m4ykey.ui.album.viewmodel.AlbumViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumStatisticsFragment : BaseFragment<FragmentAlbumStatisticsBinding>(
    FragmentAlbumStatisticsBinding::inflate
) {

    private val viewModel by viewModels<AlbumViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAlbumCount()
        viewModel.getTotalTracksCount()
        viewModel.getMostPopularDecade()
        viewModel.getAlbumTypeCount("Album")
        viewModel.getAlbumTypeCount("Single")
        viewModel.getAlbumTypeCount("EP")
        viewModel.getAlbumTypeCount("Compilation")
        viewModel.getAlbumWithMostTracks()

        binding.apply {
            toolbar.setOnClickListener { findNavController().navigateUp() }

            lifecycleScope.launch {
                viewModel.albumCount.collect { count ->
                    txtAlbumCount.text = "$count"
                }
            }

            lifecycleScope.launch {
                viewModel.totalTracksCount.collect { count ->
                    txtTracksCount.text = "$count"
                }
            }

            lifecycleScope.launch {
                viewModel.decadeResult.collect { decade ->
                    decade?.let {
                        val decadeShort = (it.decade % 100) / 10 * 10
                        txtPopularDecade.text = "$decadeShort'"
                    }
                }
            }

            lifecycleScope.launch {
                launch {
                    viewModel.albumType.collect { count ->
                        txtAlbumTypeCount.text = getString(R.string.album) + " - $count"
                    }
                }
                launch {
                    viewModel.compilationType.collect { count ->
                        txtCompilationTypeCount.text = getString(R.string.compilation) + " - $count"
                    }
                }
                launch {
                    viewModel.epType.collect { count ->
                        txtEPTypeCount.text = getString(R.string.ep) + " - $count"
                    }
                }
                launch {
                    viewModel.singleType.collect { count ->
                        txtSingleTypeCount.text = getString(R.string.single) + " - $count"
                    }
                }
            }

            lifecycleScope.launch {
                viewModel.albumWithMostTracks.collect { album ->
                    album?.let {
                        txtAlbum.text = "${it.albumName} - ${it.albumTotalTracks} ${getString(R.string.tracks)}"
                        loadImage(imgAlbumCover, it.albumImage)

                        cardViewMostSongs.isVisible = true
                    } ?: run {
                        cardViewMostSongs.isVisible = false
                    }
                }
            }
        }
    }
}